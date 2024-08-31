package ru.job4j.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;


import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ListPageTaskDto {
    private int id;
    private String title;
    private LocalDateTime created;
    private boolean done;
    private String userName;
}
