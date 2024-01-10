package com.sellbycar.marketplace.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @SuppressWarnings({"rawtypes"})
    private final PostgreSQLContainer<?> container = new PostgreSQLContainer("postgres:latest")
            .withUsername("development")
            .withPassword("development")
            .withDatabaseName("development");

    @Bean
    public DataSource dataSource() {
        container.start();
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(container.getJdbcUrl());
        dataSource.setUsername(container.getUsername());
        dataSource.setPassword(container.getPassword());
        return dataSource;
    }
}
