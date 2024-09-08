package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;
import java.util.*;
import java.util.function.Consumer;

@Repository
@AllArgsConstructor
public class HbmTaskRepository implements TaskRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(HbmTaskRepository.class);
    private final CrudRepository crudRepository;

    @Override
    public Task save(Task task) {
        try {
            crudRepository.run((Consumer<Session>) session -> session.persist(task));
        } catch (Exception e) {
            LOGGER.error("Exception on save Task", e);
        }
        return task;
    }

    @Override
    public boolean update(Task task) {
        try {
            return crudRepository.run("""
                    UPDATE Task SET description = :fDescription, created = :fCreated, done = :fDone, title = :fTitle
                    WHERE id = :fId
                    """,
                    Map.of("fDescription", task.getDescription(),
                            "fCreated", task.getCreated(),
                            "fDone", task.isDone(),
                            "fTitle", task.getTitle(),
                            "fId", task.getId()
                    ));
        } catch (Exception e) {
            LOGGER.error("Exception on update Task", e);
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        try {
            return crudRepository.run("DELETE Task WHERE id = :fId", Map.of("fId", id));
        } catch (Exception e) {
            LOGGER.error("Exception on delete Task", e);
        }
        return false;
    }

    @Override
    public Optional<Task> findById(int id) {
        try {
            return crudRepository.optional(
                    "SELECT DISTINCT t from Task t JOIN FETCH t.priority JOIN FETCH t.categories WHERE t.id = :fId",
                    Task.class,
                    Map.of("fId", id)
            );
        } catch (Exception e) {
            LOGGER.error("Exception on find Task ById", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Task> findAllOrderById() {
        try {
            return crudRepository.query(
                    "SELECT DISTINCT t from Task t JOIN FETCH t.priority JOIN FETCH t.categories ORDER BY t.id", Task.class);
        } catch (Exception e) {
            LOGGER.error("Exception on findAll Task orderById", e);
        }
        return Collections.emptyList();
    }

    @Override
    public List<Task> findByStatus(boolean status) {
        try {
            return crudRepository.query(
                    "from Task t JOIN FETCH t.priority WHERE t.done = :fDone ", Task.class, Map.of("fDone", status));
        } catch (Exception e) {
            LOGGER.error("Exception on find Task ByStatus", e);
        }
        return Collections.emptyList();
    }

    @Override
    public boolean complete(int id) {
        try {
            return crudRepository.run("UPDATE Task SET done = true WHERE id = :fId", Map.of("fId", id));
        } catch (Exception e) {
            LOGGER.error("Exception on complete Task", e);
        }
        return false;
    }
}
