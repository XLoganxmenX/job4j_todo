package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Priority;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class HbmPriorityRepository implements PriorityRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(HbmPriorityRepository.class);
    private final CrudRepository crudRepository;

    @Override
    public Optional<Priority> findById(int id) {
        try {
            return crudRepository.optional("FROM Priority WHERE id = :fId", Priority.class, Map.of("fId", id));
        } catch (Exception e) {
            LOGGER.error("Exception on find Priority ById", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Priority> findAll() {
        try {
            return crudRepository.query("FROM Priority ORDER BY id", Priority.class);
        } catch (Exception e) {
            LOGGER.error("Exception on find Priority ById", e);
        }
        return Collections.emptyList();
    }
}
