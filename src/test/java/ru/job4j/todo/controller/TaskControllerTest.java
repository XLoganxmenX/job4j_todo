package ru.job4j.todo.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.todo.dto.ListPageTaskDto;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.model.Priority;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.CategoryService;
import ru.job4j.todo.service.TaskService;
import ru.job4j.todo.service.PriorityService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TaskControllerTest {
    private static TaskController taskController;
    private static TaskService taskService;
    private static PriorityService priorityService;
    private static CategoryService categoryService;

    @BeforeAll
    public static void init() {
        taskService = mock(TaskService.class);
        priorityService = mock(PriorityService.class);
        categoryService = mock(CategoryService.class);
        taskController = new TaskController(taskService, priorityService, categoryService);
    }

    @Test
    public void whenGetAllThenReturnPageWithTasksDto() {
        var expectedTasks = List.of(
                new ListPageTaskDto(1, "task1", LocalDateTime.now(), true,
                        "user", "priority", List.of("category")),
                new ListPageTaskDto(2, "task2", LocalDateTime.now().plusHours(1), false,
                        "user", "priority", List.of("category"))
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
                new ListPageTaskDto(1, "task1", LocalDateTime.now(), true,
                        "user", "priority", List.of("category")),
                new ListPageTaskDto(2, "task2", LocalDateTime.now().plusHours(1), true,
                        "user", "priority", List.of("category"))
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
                new ListPageTaskDto(1, "task1", LocalDateTime.now(), false,
                        "user", "priority", List.of("category")),
                new ListPageTaskDto(2, "task2", LocalDateTime.now().plusHours(1), false,
                        "user", "priority", List.of("category"))
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
        var expectedTask =
                new Task(1, "task1", "task1", LocalDateTime.now(), false,
                        new User(), new Priority(), List.of());
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
    public void whenCompleteExistTaskThenReturnTaskPage() {
        var expectedTask =
                new Task(1, "task1", "task1", LocalDateTime.now(), false,
                        new User(), new Priority(), List.of());
        when(taskService.complete(expectedTask.getId())).thenReturn(true);
        var model = new ConcurrentModel();
        var view = taskController.completeTask(expectedTask.getId(), model);
        assertThat(view).isEqualTo("redirect:/tasks/1");
    }

    @Test
    public void whenCompleteNotExitsTaskThenReturnErrorMessagePage() {
        when(taskService.complete(0)).thenReturn(false);
        var model = new ConcurrentModel();
        var view = taskController.completeTask(0, model);
        var actualMessage = model.getAttribute("message");

        assertThat(actualMessage).isEqualTo("Не удалось перевести задачу в статус \"Выполнено\"");
        assertThat(view).isEqualTo("errors/404");
    }

    @Test
    public void whenDeleteExistTaskThenReturnTaskListPage() {
        when(taskService.delete(1)).thenReturn(true);
        var view = taskController.deleteTask(1, new ConcurrentModel());
        assertThat(view).isEqualTo("redirect:/tasks");
    }

    @Test
    public void whenDeleteNotExistTaskThenReturnErrorMessagePage() {
        when(taskService.delete(0)).thenReturn(false);
        var model = new ConcurrentModel();
        var view = taskController.deleteTask(0, model);
        var actualMessage = model.getAttribute("message");
        assertThat(actualMessage).isEqualTo("Не удалось удалить задачу");
        assertThat(view).isEqualTo("errors/404");
    }

    @Test
    public void whenGetEditPageThenReturnEditPageWithTask() {
        var expectedTask =
                new Task(1, "task1", "task1", LocalDateTime.now(), false,
                        new User(), new Priority(), List.of());
        when(taskService.findById(expectedTask.getId())).thenReturn(Optional.of(expectedTask));
        var model = new ConcurrentModel();
        var view = taskController.getEditPage(expectedTask.getId(), model);
        var actualTask = model.getAttribute("task");

        assertThat(actualTask).isEqualTo(expectedTask);
        assertThat(view).isEqualTo("tasks/edit");
    }

    @Test
    public void whenUpdateExistTaskThenReturnTaskListPage() {
        var task = new Task();
        when(taskService.update(task)).thenReturn(true);
        var view = taskController.update(task, new ConcurrentModel());
        assertThat(view).isEqualTo("redirect:/tasks");
    }

    @Test
    public void whenUpdateNotExistTaskThenReturnErrorMessagePage() {
        var task = new Task();
        when(taskService.update(task)).thenReturn(false);
        var model = new ConcurrentModel();
        var view = taskController.update(task, model);
        var actualMessage = model.getAttribute("message");
        assertThat(actualMessage).isEqualTo("Не удалось обновить задачу");
        assertThat(view).isEqualTo("errors/404");
    }

    @Test
    public void whenGetCreatePage() {
        var expectedCategories = List.of(new Category(1, "category"));
        var expectedPriorities = List.of(new Priority(1, "normal", 2));

        when(categoryService.findAll()).thenReturn(expectedCategories);
        when(priorityService.findAll()).thenReturn(expectedPriorities);

        var model = new ConcurrentModel();
        var view = taskController.getCreatePage(model);
        var actualCategories = model.getAttribute("categories");
        var actualPriorities = model.getAttribute("priorities");
        assertThat(actualCategories).isEqualTo(expectedCategories);
        assertThat(actualPriorities).isEqualTo(expectedPriorities);
        assertThat(view).isEqualTo("tasks/create");
    }

    @Test
    public void whenSaveThenReturnTaskListPage() {
        var view = taskController.save(new Task(), List.of(), new MockHttpSession());
        assertThat(view).isEqualTo("redirect:/tasks");
    }

}