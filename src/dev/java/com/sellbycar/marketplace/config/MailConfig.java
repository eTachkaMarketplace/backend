package com.sellbycar.marketplace.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.testcontainers.containers.GenericContainer;

import java.util.Properties;

@Configuration
public class MailConfig {

    @SuppressWarnings({"resource"})
    private final GenericContainer<?> container = new GenericContainer<>("oryd/mailslurper:latest-smtps")
            .withExposedPorts(1025);

    @Bean
    public JavaMailSender javaMailSender() {
        container.start();
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(container.getHost());
        mailSender.setPort(container.getMappedPort(1025));
        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.smtp.auth", "false");
        properties.put("mail.smtp.starttls.enable", "false");
        return mailSender;
    }
}
