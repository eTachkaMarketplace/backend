package com.sellbycar.marketplace.controllers;

import com.sellbycar.marketplace.services.AdvertisementService;
import com.sellbycar.marketplace.utilities.mapper.AdvertisementMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class AdvertisementControllerTest {

    @InjectMocks
    private AdvertisementController advertisementController;
    @Mock
    private AdvertisementService advertisementService;
    @Mock
    private AdvertisementMapper advertisementMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        advertisementController = new AdvertisementController(advertisementService, advertisementMapper);
    }

    @Test
    @Disabled
    void testShowAllAd() {
    }

    @Test
    @Disabled
    void getAdById() {
    }

    @Test
    @Disabled
    void createAd() {
    }

    @Test
    @Disabled
    void changeADv() {
    }

    @Test
    @Disabled
    void getAllFavorites() {
    }

    @Test
    @Disabled
    void addToFavoriteList() {
    }

    @Test
    @Disabled
    void removeFromFavoriteList() {
    }

    @Test
    @Disabled
    void deleteAdvertisement() {
    }
}