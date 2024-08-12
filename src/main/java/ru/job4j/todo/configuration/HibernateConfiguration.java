package ru.job4j.todo.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Component
public class HibernateConfiguration {
    @Value("${hibernate.config.file}")
    private String hibernateConfig;

    public String getHibernateConfig() {
        return hibernateConfig;
    }
}
