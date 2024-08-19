package ru.job4j.todo.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.todo.dto.ListPageTaskDto;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.service.TaskService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TaskControllerTest {
    private static TaskController taskController;
    private static TaskService taskService;

    @BeforeAll
    public static void init() {
        taskService = mock(TaskService.class);
        taskController = new TaskController(taskService);
    }

    @Test
    public void whenGetAllThenReturnPageWithTasksDto() {
        var expectedTasks = List.of(
                new ListPageTaskDto(1, "task1", LocalDateTime.now(), true),
                new ListPageTaskDto(2, "task2", LocalDateTime.now().plusHours(1), false)
        );
        when(taskService.findAllTaskDtoOrderById()).thenReturn(expectedTasks);
        var model = new ConcurrentModel();
        var view = taskController.getAll(model);
        var actualTasks = model.getAttribute("tasksDto");

        assertThat(actualTasks).isEqualTo(expectedTasks);
        assertThat(view).isEqualTo("tasks/list");
    }

    @Test
    public void whenGetDoneTaskThenReturnPageWithDoneTasks() {
        var expectedTasks = List.of(
                new ListPageTaskDto(1, "task1", LocalDateTime.now(), true),
                new ListPageTaskDto(2, "task2", LocalDateTime.now().plusHours(1), true)
        );
        when(taskService.findTasksDtoByStatus(true)).thenReturn(expectedTasks);
        var model = new ConcurrentModel();
        var view = taskController.getDoneTask(model);
        var actualTasks = model.getAttribute("tasksDto");

        assertThat(actualTasks).isEqualTo(expectedTasks);
        assertThat(view).isEqualTo("tasks/list");
    }

    @Test
    public void whenGetNewTaskThenReturnPageWithNewTasks() {
        var expectedTasks = List.of(
                new ListPageTaskDto(1, "task1", LocalDateTime.now(), false),
                new ListPageTaskDto(2, "task2", LocalDateTime.now().plusHours(1), false)
        );
        when(taskService.findTasksDtoByStatus(false)).thenReturn(expectedTasks);
        var model = new ConcurrentModel();
        var view = taskController.getNewTask(model);
        var actualTasks = model.getAttribute("tasksDto");

        assertThat(actualTasks).isEqualTo(expectedTasks);
        assertThat(view).isEqualTo("tasks/list");
    }

    @Test
    public void whenGetByIdExistTaskThenReturnTaskPage() {
        var expectedTask = new Task(1, "task1", "task1", LocalDateTime.now(), false);
        when(taskService.findById(expectedTask.getId())).thenReturn(Optional.of(expectedTask));
        var model = new ConcurrentModel();
        var view = taskController.getById(model, expectedTask.getId());
        var actualTask = model.getAttribute("task");

        assertThat(actualTask).isEqualTo(expectedTask);
        assertThat(view).isEqualTo("tasks/one");
    }

    @Test
    public void whenGetByIdNotExistTaskThenReturnErrorMessagePage() {
        when(taskService.findById(0)).thenReturn(Optional.empty());
        var model = new ConcurrentModel();
        var view = taskController.getById(model, 0);
        var actualMessage = model.getAttribute("message");

        assertThat(actualMessage).isEqualTo("Задание с указанным идентификатором не найдено");
        assertThat(view).isEqualTo("errors/404");
    }

    @Test
    public void whenCompleteTaskThenSetDoneTrueAndReturnTaskPage() {
        var expectedTask = new Task(1, "task1", "task1", LocalDateTime.now(), false);
        when(taskService.findById(expectedTask.getId())).thenReturn(Optional.of(expectedTask));
        var model = new ConcurrentModel();
        var view = taskController.deleteTask(expectedTask.getId(), model);

        assertThat(expectedTask.getDone()).isTrue();
        assertThat(view).isEqualTo("redirect:/tasks/1");
    }

    @Test
    public void whenDeleteTaskThenReturnTaskListPage() {
        var view = taskController.deleteTask(0);
        assertThat(view).isEqualTo("redirect:/tasks");
    }

    @Test
    public void whenGetEditPageThenReturnEditPageWithTask() {
        var expectedTask = new Task(1, "task1", "task1", LocalDateTime.now(), false);
        when(taskService.findById(expectedTask.getId())).thenReturn(Optional.of(expectedTask));
        var model = new ConcurrentModel();
        var view = taskController.getEditPage(expectedTask.getId(), model);

        var actualTask = model.getAttribute("task");

        assertThat(actualTask).isEqualTo(expectedTask);
        assertThat(view).isEqualTo("tasks/edit");
    }

    @Test
    public void whenUpdateTaskThenReturnTaskListPage() {
        var view = taskController.update(new Task());
        assertThat(view).isEqualTo("redirect:/tasks");
    }

    @Test
    public void whenGetCreatePage() {
        var view = taskController.getCreatePage();
        assertThat(view).isEqualTo("tasks/create");
    }

    @Test
    public void whenSaveThenReturnTaskListPage() {
        var view = taskController.save(new Task());
        assertThat(view).isEqualTo("redirect:/tasks");
    }
}