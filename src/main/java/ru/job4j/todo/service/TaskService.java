package ru.job4j.todo.service;

import ru.job4j.todo.dto.ListPageTaskDto;
import ru.job4j.todo.model.Priority;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;

import java.util.List;
import java.util.Optional;

public interface TaskService {
    Task save(Task task, User user);

    boolean update(Task task);

    boolean delete(int id);

    Optional<Task> findById(int id);

    List<Task> findAllOrderById();

    List<ListPageTaskDto> findAllTaskDtoOrderById();

    List<Task> findByStatus(boolean status);

    List<ListPageTaskDto> findTasksDtoByStatus(boolean status);

    boolean complete(int id);
}
