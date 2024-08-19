package ru.job4j.todo.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class IndexControllerTest {
    private static IndexController indexController;

    @BeforeAll
    public static void init() {
        indexController = new IndexController();
    }

    @Test
    public void whenGetIndex() {
        var view = indexController.getIndex();
        assertThat(view).isEqualTo("index");
    }
}