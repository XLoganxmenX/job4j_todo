package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Category;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class HbmCategoryRepository implements CategoryRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(HbmCategoryRepository.class);
    private final CrudRepository crudRepository;

    @Override
    public Optional<Category> findById(int id) {
        try {
            return crudRepository.optional("FROM Category WHERE id = :fId", Category.class, Map.of("fId", id));
        } catch (Exception e) {
            LOGGER.error("Exception on find Category ById", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Category> findByListOfId(List<Integer> categoriesId) {
        try {
            return crudRepository.query(
                    "FROM Category WHERE id IN :fCategoriesId", Category.class,
                    Map.of("fCategoriesId", categoriesId)
            );
        } catch (Exception e) {
            LOGGER.error("Exception on find Category ById", e);
        }
        return Collections.emptyList();
    }

    @Override
    public List<Category> findAll() {
        try {
            return crudRepository.query("FROM Category ORDER BY id", Category.class);
        } catch (Exception e) {
            LOGGER.error("Exception on find Category ById", e);
        }
        return Collections.emptyList();
    }
}
