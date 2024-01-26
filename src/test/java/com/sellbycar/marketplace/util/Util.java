package com.sellbycar.marketplace.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.sellbycar.marketplace.auth.JwtResponse;
import com.sellbycar.marketplace.auth.LoginRequest;
import com.sellbycar.marketplace.web.ResponseBody;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.sql.DataSource;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@Slf4j
public class Util {

    private static final Gson gson = new Gson();

    @SneakyThrows
    public static JwtResponse authenticate(MockMvc mvc, String email, String password) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);
        String responseContent = mvc.perform(
                post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(loginRequest))
        ).andReturn().getResponse().getContentAsString();

        return gson.fromJson(gson.fromJson(responseContent, JsonObject.class).get("data"), JwtResponse.class);
    }

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
