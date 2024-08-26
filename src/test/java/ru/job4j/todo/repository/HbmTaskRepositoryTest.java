package ru.job4j.todo.repository;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.job4j.todo.Main;
import ru.job4j.todo.model.Task;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

class HbmTaskRepositoryTest {

    private static TaskRepository taskRepository;

    @BeforeAll
    public static void init() {
        ApplicationContext context = new AnnotationConfigApplicationContext(Main.class);
        SessionFactory sf = context.getBean(SessionFactory.class);
        taskRepository = new HbmTaskRepository(sf);
    }

    @AfterEach
    public void deleteAll() {
        taskRepository.findAllOrderById().forEach(task -> taskRepository.delete(task.getId()));
    }

    @Test
    public void whenSaveTaskAndFindById() {
        var task = taskRepository.save(
                new Task(0, "task", "description", LocalDateTime.now(), true)
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
    public void whenUpdateNotExistThenGetFalse() {
        var updateResult = taskRepository.update(
                new Task(0, "task", "description", LocalDateTime.now(), true)
        );
        assertThat(updateResult).isFalse();
    }

    @Test
    public void whenUpdateAndThenGetSame() {
        var task = taskRepository.save(
                new Task(0, "task", "description", LocalDateTime.now(), true)
        );
        task.setDescription("new task");
        task.setDone(false);
        var updateResult = taskRepository.update(task);
        var actualTask = taskRepository.findById(task.getId()).get();
        assertThat(actualTask).isEqualTo(task);
        assertThat(updateResult).isTrue();
    }

    @Test
    public void whenDeleteThenNotFound()  {
        var task = taskRepository.save(
                new Task(0, "task", "description", LocalDateTime.now(), true)
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
    public void whenFindAllOrderById() {
        var task1 = taskRepository.save(new Task(0, "task1", "description1", LocalDateTime.now(), true));
        var task2 = taskRepository.save(new Task(0, "task2", "description2", LocalDateTime.now().plusHours(1), false));
        var task3 = taskRepository.save(new Task(0, "task3", "description3", LocalDateTime.now().plusHours(2), true));
        var expectedList = List.of(task1, task2, task3);
        var actualList = taskRepository.findAllOrderById();
        assertThat(actualList).containsExactlyElementsOf(expectedList);
    }

    @Test
    public void whenFindByStatus() {
        var task1 = taskRepository.save(new Task(0, "task1", "description1", LocalDateTime.now(), true));
        var task2 = taskRepository.save(new Task(0, "task2", "description1", LocalDateTime.now().plusHours(1), false));
        var task3 = taskRepository.save(new Task(0, "task3", "description1", LocalDateTime.now().plusHours(2), true));
        var expectedList = List.of(task1, task3);
        var actualList = taskRepository.findByStatus(true);
        assertThat(actualList).isEqualTo(expectedList);
    }

    @Test
    public void whenCompleteExistThenTaskDoneTrue() {
        var task = taskRepository.save(
                new Task(0, "task", "description", LocalDateTime.now(), false)
        );
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