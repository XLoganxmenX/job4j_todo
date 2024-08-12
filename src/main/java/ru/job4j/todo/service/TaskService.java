package ru.job4j.todo.service;

import ru.job4j.todo.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskService {
    Task save(Task task);

    void update(Task task);

    void delete(int id);

    Optional<Task> findById(int id);

    List<Task> findAllOrderById();

    List<Task> findByStatus(boolean status);
}
