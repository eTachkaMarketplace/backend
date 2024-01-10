package com.sellbycar.marketplace.util;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;

@SuppressWarnings("resource")
public class ContainerUtil {

    public static PostgreSQLContainer<?> createPostgres() {
        return new PostgreSQLContainer<>("postgres:latest")
                .withUsername("test")
                .withPassword("test")
                .withDatabaseName("test");
    }

    public static GenericContainer<?> createMailslurper() {
        return new GenericContainer<>("oryd/mailslurper:latest-smtps")
                .withExposedPorts(1025);
    }
}
