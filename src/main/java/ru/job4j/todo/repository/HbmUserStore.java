package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;

import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

@Repository
@AllArgsConstructor
public class HbmUserStore implements UserStore {
    private static final Logger LOGGER = LoggerFactory.getLogger(HbmUserStore.class);
    private final CrudRepository crudRepository;

    @Override
    public Optional<User> save(User user) {
        try {
            crudRepository.run((Consumer<Session>) session -> session.persist(user));
            return Optional.of(user);
        } catch (Exception e) {
            LOGGER.error("Exception on save User", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) {
        try {
            return crudRepository.optional("FROM User WHERE login = :fLogin AND password = :fPassword", User.class,
                    Map.of("fLogin", login,
                            "fPassword", password
                    ));
        } catch (Exception e) {
            LOGGER.error("Exception on find User ByLoginAndPassword", e);
        }
        return Optional.empty();
    }
}
