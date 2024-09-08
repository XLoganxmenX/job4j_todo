package ru.job4j.todo.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.job4j.todo.dto.ListPageTaskDto;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.model.Task;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "user.name", target = "userName")
    @Mapping(source = "priority.name", target = "priorityName")
    @Mapping(source = "categories", target = "categories")
    ListPageTaskDto getListPageDtoFromTask(Task task);

    default List<String> mapCategories(List<Category> categories) {
        return categories.stream()
                .map(Category::getName)
                .toList();
    }
}
