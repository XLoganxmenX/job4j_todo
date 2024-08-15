package ru.job4j.todo.mappers;

import org.mapstruct.Mapper;
import ru.job4j.todo.dto.ListPageTaskDto;
import ru.job4j.todo.model.Task;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    ListPageTaskDto getListPageDtoFromTask(Task task);
    Task getTaskFromListPageDto(ListPageTaskDto taskDto);
}
