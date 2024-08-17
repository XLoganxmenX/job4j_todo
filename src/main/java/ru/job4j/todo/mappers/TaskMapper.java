package ru.job4j.todo.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.job4j.todo.dto.ListPageTaskDto;
import ru.job4j.todo.model.Task;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    @Mapping(source = "id", target = "id")
    ListPageTaskDto getListPageDtoFromTask(Task task);

    @Mapping(source = "id", target = "id")
    Task getTaskFromListPageDto(ListPageTaskDto taskDto);
}
