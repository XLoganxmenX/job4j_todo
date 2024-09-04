package ru.job4j.todo.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.job4j.todo.dto.ListPageTaskDto;
import ru.job4j.todo.model.Task;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "user.name", target = "userName")
    @Mapping(source = "priority.name", target = "priorityName")
    ListPageTaskDto getListPageDtoFromTask(Task task);
}
