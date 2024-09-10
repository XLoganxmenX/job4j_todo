package ru.job4j.todo.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.todo.model.User;
import ru.job4j.todo.repository.UserStore;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SimpleUserServiceTest {
    private static UserService userService;
    private static UserStore userStore;

    @BeforeAll
    public static void init() {
        userStore = mock(UserStore.class);
        userService = new SimpleUserService(userStore);
    }

    @Test
    public void whenSaveThenGetUserOptional() {
        var user = new User(0, "Test", "login", "password", "UTC+3");
        when(userStore.save(user)).thenReturn(Optional.of(user));
        var actualUserOptional = userService.save(user);
        assertThat(actualUserOptional).usingRecursiveComparison().isEqualTo(Optional.of(user));
    }

    @Test
    public void whenSaveIncorrectUserThenGetEmptyOptional() {
        var user = new User();
        when(userStore.save(user)).thenReturn(Optional.empty());
        var actualUserOptional = userService.save(user);
        assertThat(actualUserOptional).isEmpty();
    }

    @Test
    public void whenFindByLoginAndPassThenGetUserOptional() {
        var user = new User(0, "Test", "login", "password", "UTC+3");
        when(userStore.findByLoginAndPassword(user.getLogin(), user.getPassword()))
                .thenReturn(Optional.of(user));
        var actualUserOptional = userService.findByLoginAndPassword(user.getLogin(), user.getPassword());
        assertThat(actualUserOptional).usingRecursiveComparison().isEqualTo(Optional.of(user));
    }

    @Test
    public void whenFindByLoginAndPassIncorrectUserThenEmptyOptional() {
        var user = new User(0, "Incorrect", "Incorrect", "Incorrect", "UTC+3");
        when(userStore.findByLoginAndPassword(user.getLogin(), user.getPassword()))
                .thenReturn(Optional.empty());
        var actualUserOptional = userService.findByLoginAndPassword(user.getLogin(), user.getPassword());
        assertThat(actualUserOptional).isEmpty();
    }
}