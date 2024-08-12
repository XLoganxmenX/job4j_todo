package ru.job4j.todo;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.job4j.todo.configuration.HibernateConfiguration;

@SpringBootApplication
public class Main {

    @Bean(destroyMethod = "close")
    public SessionFactory sf() {
        String hibernateConfig = new HibernateConfiguration().getHibernateConfig();
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure(hibernateConfig).build();
        return new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}