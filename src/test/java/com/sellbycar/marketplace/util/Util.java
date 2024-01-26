package com.sellbycar.marketplace.util;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
public class Util {

    @SneakyThrows
    public static void injectTestData(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = String.join("\n", Files.readAllLines(Path.of("scripts/test-data.sql")));
            try (CallableStatement statement = connection.prepareCall(sql)) {
                statement.execute();
            }
            log.info("Test data initialized.");
        } catch (SQLException e) {
            log.error("Failed to initialize test data.", e);
        }
    }
}
