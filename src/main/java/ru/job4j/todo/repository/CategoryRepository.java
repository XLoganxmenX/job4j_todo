package ru.job4j.todo.repository;

import ru.job4j.todo.model.Category;
import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
    Optional<Category> findById(int id);

    List<Category> findByListOfId(List<Integer> categoriesId);

    List<Category> findAll();
}
