package com.sellbycar.marketplace.ad;

import com.sellbycar.marketplace.util.ConfiguredSpringBootTest;
import com.sellbycar.marketplace.util.Util;
import lombok.SneakyThrows;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.sql.DataSource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", Matchers.hasSize(Matchers.greaterThan(0))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[*].car.brand", Matchers.everyItem(Matchers.is("BMW"))));
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
    void createAdvertisement() {
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
    void addToFavoriteList() {
    }

    @Test
    void removeFromFavoriteList() {
    }
}
