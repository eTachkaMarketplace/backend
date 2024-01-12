package com.sellbycar.marketplace.data;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

@Component
@RequiredArgsConstructor
@Slf4j
public class TestDataProvider implements ApplicationListener<ApplicationReadyEvent> {

    private final DataSource dataSource;

    @Override
    public void onApplicationEvent(@NotNull ApplicationReadyEvent event) {
        try (Connection connection = dataSource.getConnection()) {
            insertTestData(connection);
            log.info("Test data initialized.");
        } catch (SQLException e) {
            log.error("Failed to initialize test data.", e);
        }
    }

    @SneakyThrows
    private void insertTestData(Connection connection) {
        String sql = String.join("\n", Files.readAllLines(Path.of("scripts/test-data.sql")));
        try (CallableStatement statement = connection.prepareCall(sql)) {
            statement.execute();
        }
    }

    @Override
    public boolean supportsAsyncExecution() {
        return true;
    }
}
