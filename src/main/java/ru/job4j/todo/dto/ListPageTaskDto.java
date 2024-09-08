package ru.job4j.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;


import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class ListPageTaskDto {
    private int id;
    private String title;
    private LocalDateTime created;
    private boolean done;
    private String userName;
    private String priorityName;
    private List<String> categories;
}
