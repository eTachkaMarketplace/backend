package com.sellbycar.marketplace.util;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class ConfiguredSpringBootTest {

    @Container
    public static PostgreSQLContainer<?> postgres = ContainerUtil.createPostgres();
    @Container
    public static GenericContainer<?> mailslurper = ContainerUtil.createMailslurper();

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        // Database
        postgres.start();
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        // Mail
        mailslurper.start();
        registry.add("spring.mail.host", mailslurper::getHost);
        registry.add("spring.mail.port", () -> mailslurper.getMappedPort(1025));
        registry.add("spring.mail.username", () -> "");
        registry.add("spring.mail.protocol", () -> "smtp");
        registry.add("spring.mail.smtp.auth", () -> false);
        registry.add("spring.mail.smtp.starttls.enable", () -> false);
    }
}
