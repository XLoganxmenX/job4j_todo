package ru.job4j.todo.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.todo.mappers.TaskMapper;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.repository.TaskRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SimpleTaskServiceTest {
    private static TaskService taskService;
    private static TaskRepository taskRepository;

    @BeforeAll
    public static void init() {
        taskRepository = mock(TaskRepository.class);
        TaskMapper taskMapper = mock(TaskMapper.class);
        taskService = new SimpleTaskService(taskRepository, taskMapper);
    }

    @Test
    public void whenSave() {
        var task = new Task(1, "task", "description", LocalDateTime.now(), true);
        when(taskRepository.save(task)).thenReturn(task);
        var actualTask = taskService.save(task);
        assertThat(actualTask).isEqualTo(task);
    }

    @Test
    public void whenFindByIdExistThenGetTaskOptional() {
        var task = new Task(1, "task", "description", LocalDateTime.now(), true);
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        var actualTask = taskService.findById(task.getId());
        assertThat(actualTask).isEqualTo(Optional.of(task));
    }

    @Test
    public void whenFindByIdNotExistThenGetEmptyOptional() {
        when(taskRepository.findById(0)).thenReturn(Optional.empty());
        var actualTask = taskService.findById(0);
        assertThat(actualTask).isEmpty();
    }

    @Test
    public void whenFindAllOrderById() {
        var task1 = new Task(1, "task1", "description1", LocalDateTime.now(), true);
        var task2 = new Task(2, "task2", "description2", LocalDateTime.now().plusHours(1), false);
        var task3 = new Task(3, "task3", "description3", LocalDateTime.now().plusHours(2), true);
        var expectedList = List.of(task1, task2, task3);
        when(taskRepository.findAllOrderById()).thenReturn(expectedList);
        var actualList = taskService.findAllOrderById();
        assertThat(actualList).containsExactlyElementsOf(expectedList);
    }

    @Test
    public void whenFindByStatus() {
        var task1 = new Task(1, "task1", "description1", LocalDateTime.now(), true);
        var task2 = new Task(2, "task2", "description2", LocalDateTime.now().plusHours(1), false);
        var task3 = new Task(3, "task3", "description3", LocalDateTime.now().plusHours(2), true);
        var expectedList = List.of(task1, task3);
        when(taskRepository.findByStatus(true)).thenReturn(expectedList);
        var actualList = taskService.findByStatus(true);
        assertThat(actualList).isEqualTo(expectedList);
    }
}