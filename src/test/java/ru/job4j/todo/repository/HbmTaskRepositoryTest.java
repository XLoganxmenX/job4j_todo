package ru.job4j.todo.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.job4j.todo.Main;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.model.Priority;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

class HbmTaskRepositoryTest {
    private static TaskRepository taskRepository;
    private static CrudRepository crudRepository;

    @BeforeAll
    public static void init() {
        ApplicationContext context = new AnnotationConfigApplicationContext(Main.class);
        SessionFactory sf = context.getBean(SessionFactory.class);
        crudRepository = new CrudRepository(sf);
        taskRepository = new HbmTaskRepository(crudRepository);
    }

    @AfterEach
    public void deleteAll() throws Exception {
        taskRepository.findAllOrderById().forEach(task -> taskRepository.delete(task.getId()));
        crudRepository.run("DELETE User", Map.of());
        crudRepository.run("DELETE Priority", Map.of());
        crudRepository.run("DELETE Category", Map.of());
    }

    @Test
    public void whenSaveTaskAndFindById() throws Exception {
        var user = new User(0, "Test", "login", "password", "UTC+3");
        var priority = new Priority(0, "Test", 1);
        var category = new Category(0, "category");
        crudRepository.run((Consumer<Session>) session -> session.persist(category));
        crudRepository.run((Consumer<Session>) session -> session.persist(user));
        crudRepository.run((Consumer<Session>) session -> session.persist(priority));
        var task = taskRepository.save(
                new Task(0, "task", "description", LocalDateTime.now(), true,
                        user, priority, List.of(category))
        );
        var savedTask = taskRepository.findById(task.getId()).get();
        assertThat(task).isEqualTo(savedTask);
    }

    @Test
    public void whenFindByIdNotExistTask() {
        var actualTask = taskRepository.findById(0);
        assertThat(actualTask).isEmpty();
    }

    @Test
    public void whenSaveTaskAndFindByIdThenUserEqual() throws Exception {
        var expectedUser = new User(0, "Test", "login", "password", "UTC+3");
        var priority = new Priority(0, "Test", 1);
        var category = new Category(0, "category");
        crudRepository.run((Consumer<Session>) session -> session.persist(category));
        crudRepository.run((Consumer<Session>) session -> session.persist(priority));
        crudRepository.run((Consumer<Session>) session -> session.persist(expectedUser));
        var task = taskRepository.save(
                new Task(0, "task", "description", LocalDateTime.now(), true,
                        expectedUser, priority, List.of(category))
        );
        var savedTask = taskRepository.findById(task.getId()).get();
        var actualUser = savedTask.getUser();
        assertThat(actualUser).usingRecursiveComparison().isEqualTo(expectedUser);
    }

    @Test
    public void whenUpdateNotExistThenGetFalse() {
        var updateResult = taskRepository.update(
                new Task(0, "task", "description", LocalDateTime.now(), true,
                        new User(), new Priority(), List.of())
        );
        assertThat(updateResult).isFalse();
    }

    @Test
    public void whenUpdateAndThenGetSame() throws Exception {
        var user = new User(0, "Test", "login", "password", "UTC+3");
        var priority = new Priority(0, "Test", 1);
        var category = new Category(0, "category");
        crudRepository.run((Consumer<Session>) session -> session.persist(category));
        crudRepository.run((Consumer<Session>) session -> session.persist(priority));
        crudRepository.run((Consumer<Session>) session -> session.persist(user));

        var task = taskRepository.save(
                new Task(0, "task", "description", LocalDateTime.now(), true,
                        user, priority, List.of(category))
        );
        task.setDescription("new task");
        task.setDone(false);
        var updateResult = taskRepository.update(task);
        var actualTask = taskRepository.findById(task.getId()).get();
        assertThat(actualTask).isEqualTo(task);
        assertThat(updateResult).isTrue();
    }

    @Test
    public void whenDeleteThenNotFound() throws Exception {
        var user = new User(0, "Test", "login", "password", "UTC+3");
        var priority = new Priority(0, "Test", 1);
        var category = new Category(0, "category");
        crudRepository.run((Consumer<Session>) session -> session.persist(category));
        crudRepository.run((Consumer<Session>) session -> session.persist(priority));
        crudRepository.run((Consumer<Session>) session -> session.persist(user));
        var task = taskRepository.save(
                new Task(0, "task", "description", LocalDateTime.now(), true,
                        user, priority, List.of(category))
        );
        var deleteResult = taskRepository.delete(task.getId());
        var actualTask = taskRepository.findById(task.getId());
        assertThat(actualTask).isEmpty();
        assertThat(deleteResult).isTrue();
    }

    @Test
    public void whenDeleteNotExistThenGetFalse()  {
        var deleteResult = taskRepository.delete(0);
        assertThat(deleteResult).isFalse();
    }

    @Test
    public void whenFindAllOrderById() throws Exception {
        var user = new User(0, "Test", "login", "password", "UTC+3");
        var priority = new Priority(0, "Test", 1);
        var category = new Category(0, "category");
        crudRepository.run((Consumer<Session>) session -> session.persist(category));
        crudRepository.run((Consumer<Session>) session -> session.persist(priority));
        crudRepository.run((Consumer<Session>) session -> session.persist(user));
        var task1 = taskRepository.save(
                new Task(0, "task1", "description1", LocalDateTime.now(), true, user, priority, List.of(category)));
        var task2 = taskRepository.save(
                new Task(0, "task2", "description2", LocalDateTime.now().plusHours(1), false, user, priority, List.of(category)));
        var task3 = taskRepository.save(
                new Task(0, "task3", "description3", LocalDateTime.now().plusHours(2), true, user, priority, List.of(category)));
        var expectedList = List.of(task1, task2, task3);
        var actualList = taskRepository.findAllOrderById();
        assertThat(actualList).containsExactlyElementsOf(expectedList);
    }

    @Test
    public void whenFindByStatus() throws Exception {
        var user = new User(0, "Test", "login", "password", "UTC+3");
        var priority = new Priority(0, "Test", 1);
        var category = new Category(0, "category");
        crudRepository.run((Consumer<Session>) session -> session.persist(category));
        crudRepository.run((Consumer<Session>) session -> session.persist(priority));
        crudRepository.run((Consumer<Session>) session -> session.persist(user));
        var task1 = taskRepository.save(
                new Task(0, "task1", "description1", LocalDateTime.now(), true,
                        user, priority, List.of(category)));
        var task2 = taskRepository.save(
                new Task(0, "task2", "description1", LocalDateTime.now().plusHours(1), false,
                        user, priority, List.of(category)));
        var task3 = taskRepository.save(
                new Task(0, "task3", "description1", LocalDateTime.now().plusHours(2), true,
                        user, priority, List.of(category)));
        var expectedList = List.of(task1, task3);
        var actualList = taskRepository.findByStatus(true);
        assertThat(actualList).isEqualTo(expectedList);
    }

    @Test
    public void whenCompleteExistThenTaskDoneTrue() throws Exception {
        var user = new User(0, "Test", "login", "password", "UTC+3");
        var priority = new Priority(0, "Test", 1);
        var category = new Category(0, "category");
        crudRepository.run((Consumer<Session>) session -> session.persist(category));
        crudRepository.run((Consumer<Session>) session -> session.persist(priority));
        crudRepository.run((Consumer<Session>) session -> session.persist(user));
        var task = taskRepository.save(
                new Task(0, "task", "description", LocalDateTime.now(), false,
                        user, priority, List.of(category)));
        var updateResult = taskRepository.complete(task.getId());
        var actualTask = taskRepository.findById(task.getId()).get();
        assertThat(actualTask.isDone()).isTrue();
        assertThat(updateResult).isTrue();
    }

    @Test
    public void whenCompleteNotExistThenGetFalse() {
        var updateResult = taskRepository.complete(0);
        assertThat(updateResult).isFalse();
    }
}