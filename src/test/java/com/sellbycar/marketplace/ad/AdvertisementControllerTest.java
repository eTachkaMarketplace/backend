package com.sellbycar.marketplace.ad;

import com.sellbycar.marketplace.auth.JwtResponse;
import com.sellbycar.marketplace.auth.JwtUtils;
import com.sellbycar.marketplace.util.ConfiguredSpringBootTest;
import com.sellbycar.marketplace.util.Util;
import lombok.SneakyThrows;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.multipart.MultipartFile;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import javax.sql.DataSource;

import java.util.ArrayList;
import java.util.List;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AdvertisementControllerTest extends ConfiguredSpringBootTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private DataSource dataSource;

    @BeforeEach
    void injectTestData() {
        Util.injectTestData(dataSource);
    }

    @Test
    @SneakyThrows
    void searchAdvertisement() {
        mvc.perform(
                        get("/advertisement/search")
                                .queryParam("sort", "old")
                                .queryParam("page", "0")
                                .queryParam("size", "10")
                                .queryParam("brand", "BMW")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.data", Matchers.hasSize(Matchers.greaterThan(0))))
                .andExpect(jsonPath("$.data[*].car.brand", Matchers.everyItem(Matchers.is("BMW"))));
        mvc.perform(
                get("/advertisement/search")
                        .queryParam("sort", "old")
                        .queryParam("page", "0")
                        .queryParam("size", "10")
                        .queryParam("filter", "{}")
        ).andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void getAdvertisementById() {
        mvc.perform(
                get("/advertisement/{id}", Integer.MAX_VALUE)
        ).andExpect(status().isNotFound());
        mvc.perform(
                get("/advertisement/{id}", 1)
        ).andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void createAdvertisement() {
        JwtResponse jwt = Util.authenticate(mvc, "user@test.com", "test");
        MockMultipartFile payload = new MockMultipartFile("payload", "", "application/json",
                ("{\"description\":\"Test Advertisement\",\"region\":\"Test Region\"," +
                        "\"city\":\"Test City\",\"category\":\"Test Category\"," +
                        "\"car\":{\"brand\":\"Test Brand\",\"model\":\"Test Model\"," +
                        "\"vin\":\"Test VIN\",\"year\":2022," +
                        "\"price\":10000,\"licensePlate\":\"Test\"," +
                        "\"mileage\":50000,\"transmissionType\":\"Automatic\"," +
                        "\"engineType\":\"Petrol\",\"engineVolume\":2.0,\"technicalState\":\"Good\"," +
                        "\"bodyType\":\"Sedan\",\"driveType\":\"Front\"," +
                        "\"color\":\"Red\"},\"contactName\":\"Test Contact\"," +
                        "\"contactPhone\":\"1234567890\"," +
                        "\"previewImage\":\"https://example.com/preview.jpg\"," +
                        "\"images\":[\"https://example.com/image1.jpg\"," +
                        "\"https://example.com/image2.jpg\"]," +
                        "\"createdTimestamp\":\"2024-02-10T09:25:00.699Z\"," +
                        "\"active\":true}").getBytes());
        MockMultipartFile image1 = new MockMultipartFile("images", "image1.jpg", "image/jpeg", "image1".getBytes());
        MockMultipartFile image2 = new MockMultipartFile("images", "image2.jpg", "image/jpeg", "image2".getBytes());

        mvc.perform(
                        MockMvcRequestBuilders.multipart("/advertisement")
                                .file(payload)
                                .file(image1)
                                .file(image2)
                                .header("Authorization", "Bearer " + jwt.getJwtAccessToken())
                )
                .andExpect(status().isCreated());
    }

    @Test
    void changeAdvertisement() {
    }


    @Test
    void deleteAdvertisement() {
    }

    @Test
    void getAllFavorites() {
    }

    @Test
    @SneakyThrows
    void addToFavoriteList() {
        JwtResponse jwt = Util.authenticate(mvc, "user@test.com", "test");
        mvc.perform(
                post("/advertisement/favorite/{id}", 1)
                        .header("Authorization", "Bearer " + jwt.getJwtAccessToken())
        ).andExpect(status().isCreated());
    }

    @Test
    @SneakyThrows
    void removeFavoriteFromList() {
        JwtResponse jwt = Util.authenticate(mvc, "user@test.com", "test");
        mvc.perform(
                post("/advertisement/favorite/{id}", 1)
                        .header("Authorization", "Bearer " + jwt.getJwtAccessToken())
        ).andExpect(status().isCreated());
        mvc.perform(
                delete("/advertisement/favorite/{id}", 1)
                        .header("Authorization", "Bearer " + jwt.getJwtAccessToken())
        ).andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void getFavoriteList() {
        JwtResponse jwt = Util.authenticate(mvc, "user@test.com", "test");
        mvc.perform(
                post("/advertisement/favorite/{id}", 1)
                        .header("Authorization", "Bearer " + jwt.getJwtAccessToken())
        ).andExpect(status().isCreated());
        mvc.perform(
                        get("/advertisement/favorite")
                                .header("Authorization", "Bearer " + jwt.getJwtAccessToken())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", Matchers.hasSize(1)));
    }

    @Test
    void removeFromFavoriteList() {
    }
}
