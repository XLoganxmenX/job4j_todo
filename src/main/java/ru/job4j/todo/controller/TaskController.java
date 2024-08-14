package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.todo.dto.ListPageTaskDto;
import ru.job4j.todo.service.TaskService;

@Controller
@RequestMapping("/tasks")
@AllArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("tasksDto", taskService.findAllOrderById().stream()
                .map(task -> new ListPageTaskDto(task.getTitle(), task.getCreated(), task.getDone()))
                .toList());
        return "tasks/list";
    }
}
