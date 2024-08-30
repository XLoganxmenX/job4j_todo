package ru.job4j.todo.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.job4j.todo.Main;
import ru.job4j.todo.model.User;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class HbmUserStoreTest {

    private static UserStore userStore;
    private static CrudRepository crudRepository;

    @BeforeAll
    public static void init() {
        ApplicationContext context = new AnnotationConfigApplicationContext(Main.class);
        SessionFactory sf = context.getBean(SessionFactory.class);
        crudRepository = new CrudRepository(sf);
        userStore = new HbmUserStore(crudRepository);
    }

    @BeforeEach
    public void deleteAll() throws Exception {
        crudRepository.run("DELETE User", Map.of());
    }

    @Test
    public void whenSaveThenGetUserOptional() {
        var user = new User(0, "Test", "login", "password");
        var actualOptionalUser = userStore.save(user);
        assertThat(actualOptionalUser).isEqualTo(Optional.of(user));
    }

    @Test
    public void whenSaveIncorrectUserThenGetEmptyOptional() {
        var actualOptionalUser = userStore.save(new User());
        assertThat(actualOptionalUser).isEqualTo(Optional.empty());
    }

    @Test
    public void whenSaveAndFindByLoginAndPass() {
        var savedUser = userStore.save(
                new User(0, "Test", "login", "password")
        ).get();
        var actualUser = userStore.findByLoginAndPassword(savedUser.getLogin(), savedUser.getPassword()).get();
        assertThat(actualUser).isEqualTo(savedUser);
    }

    @Test
    public void whenFindByLoginAndPasswordNotExistUser() {
        var actualUserOptional = userStore.findByLoginAndPassword("not exist", "not exist");
        assertThat(actualUserOptional).isEqualTo(Optional.empty());
    }
}