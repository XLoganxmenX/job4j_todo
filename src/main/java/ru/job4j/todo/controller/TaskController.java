package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.service.TaskService;

@Controller
@RequestMapping("/tasks")
@AllArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("tasksDto", taskService.findAllTaskDtoOrderById());
        return "tasks/list";
    }

    @GetMapping("/done")
    public String getDoneTask(Model model) {
        model.addAttribute("tasksDto", taskService.findTasksDtoByStatus(true));
        return "tasks/list";
    }

    @GetMapping("/new")
    public String getNewTask(Model model) {
        model.addAttribute("tasksDto", taskService.findTasksDtoByStatus(false));
        return "tasks/list";
    }

    @GetMapping("/{id}")
    public String getById(Model model, @PathVariable int id) {
        var taskOptional = taskService.findById(id);
        if (taskOptional.isEmpty()) {
            model.addAttribute("message", "Задание с указанным идентификатором не найдено");
            return "errors/404";
        }
        model.addAttribute("task", taskOptional.get());
        return "tasks/one";
    }

    @PostMapping("/complete/{id}")
    public String deleteTask(@PathVariable int id, Model model) {
        var taskOptional = taskService.findById(id);
        if (taskOptional.isEmpty()) {
            model.addAttribute("message", "Задание с указанным идентификатором не найдено");
            return "errors/404";
        }
        Task task = taskOptional.get();
        task.setDone(true);
        taskService.update(task);
        return "redirect:/tasks/" + id;
    }

    @PostMapping("/delete/{id}")
    public String deleteTask(@PathVariable int id) {
        taskService.delete(id);
        return "redirect:/tasks";
    }

    @GetMapping("/edit/{id}")
    public String getEditPage(@PathVariable int id, Model model) {
        var taskOptional = taskService.findById(id);
        if (taskOptional.isEmpty()) {
            model.addAttribute("message", "Задание с указанным идентификатором не найдено");
            return "errors/404";
        }
        model.addAttribute("task", taskOptional.get());
        return "tasks/edit";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Task task) {
        taskService.update(task);
        return "redirect:/tasks";
    }

    @GetMapping("/create")
    public String getCreatePage() {
        return "tasks/create";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Task task) {
        taskService.save(task);
        return "redirect:/tasks";
    }
}
