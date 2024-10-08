package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.dto.ListPageTaskDto;
import ru.job4j.todo.mappers.TaskMapper;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.model.Priority;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
import ru.job4j.todo.repository.TaskRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SimpleTaskService implements TaskService {
    private final TaskRepository taskRepository;
    private final CategoryService categoryService;
    private final TaskMapper taskMapper;

    @Override
    public Task save(Task task, User user, List<Integer> categoriesId) {
        List<Category> categories = categoryService.findByListOfId(categoriesId);
        task.setUser(user);
        task.setCategories(categories);
        return taskRepository.save(task);
    }

    @Override
    public boolean update(Task task) {
        return taskRepository.update(task);
    }

    @Override
    public boolean delete(int id) {
        return taskRepository.delete(id);
    }

    @Override
    public Optional<Task> findById(int id) {
        return taskRepository.findById(id);
    }

    @Override
    public List<Task> findAllOrderById() {
        return taskRepository.findAllOrderById();
    }

    @Override
    public List<ListPageTaskDto> findAllTaskDtoOrderById() {
        var tasks = taskRepository.findAllOrderById();
        return tasks.stream().map(taskMapper::getListPageDtoFromTask).toList();
    }

    @Override
    public List<Task> findByStatus(boolean status) {
        return taskRepository.findByStatus(status);
    }

    @Override
    public List<ListPageTaskDto> findTasksDtoByStatus(boolean status) {
        return taskRepository.findByStatus(status).stream().map(taskMapper::getListPageDtoFromTask).toList();
    }

    @Override
    public boolean complete(int id) {
        return taskRepository.complete(id);
    }
}
