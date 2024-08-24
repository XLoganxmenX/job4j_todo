package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class HbmTaskRepository implements TaskRepository {
    private final SessionFactory sf;

    @Override
    public Task save(Task task) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.save(task);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return task;
    }

    @Override
    public boolean update(Task task) {
        Session session = sf.openSession();
        boolean isUpdated = false;
        try {
            session.beginTransaction();
            if (session.get(Task.class, task.getId()) == null) {
                return false;
            }
            session.clear();
            session.update(task);
            session.getTransaction().commit();
            isUpdated = true;
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return isUpdated;
    }

    @Override
    public boolean delete(int id) {
        Session session = sf.openSession();
        boolean isDeleted = false;
        try {
            session.beginTransaction();
            if (session.get(Task.class, id) == null) {
                return false;
            }
            session.clear();
            session.createQuery("DELETE Task WHERE id = :fId")
                    .setParameter("fId", id)
                    .executeUpdate();
            session.getTransaction().commit();
            isDeleted = true;
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return isDeleted;
    }

    @Override
    public Optional<Task> findById(int id) {
        Optional<Task> optionalTask = Optional.empty();
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            Query<Task> query = session.createQuery("from Task WHERE id = :fId", Task.class)
                    .setParameter("fId", id);
            optionalTask = query.uniqueResultOptional();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return optionalTask;
    }

    @Override
    public List<Task> findAllOrderById() {
        List<Task> tasks = new ArrayList<>();
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            Query<Task> query = session.createQuery("from Task ORDER BY id", Task.class);
            tasks = query.getResultList();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return tasks;
    }

    @Override
    public List<Task> findByStatus(boolean status) {
        List<Task> tasks = new ArrayList<>();
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            Query<Task> query = session.createQuery("from Task WHERE done = :fDone", Task.class)
                    .setParameter("fDone", status);
            tasks = query.getResultList();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return tasks;
    }

    @Override
    public boolean complete(int id) {
        Session session = sf.openSession();
        boolean isComplete = false;
        try {
            session.beginTransaction();
            if (session.get(Task.class, id) == null) {
                return false;
            }
            session.clear();
            session.createQuery("UPDATE Task SET done = true WHERE id = :fId")
                    .setParameter("fId", id)
                    .executeUpdate();
            session.getTransaction().commit();
            isComplete = true;
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return isComplete;
    }
}
