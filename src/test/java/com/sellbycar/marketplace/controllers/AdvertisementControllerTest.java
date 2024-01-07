package com.sellbycar.marketplace.controllers;

import com.sellbycar.marketplace.models.dto.AdvertisementDTO;
import com.sellbycar.marketplace.models.entities.Advertisement;
import com.sellbycar.marketplace.services.AdvertisementService;
import com.sellbycar.marketplace.utilities.mapper.AdvertisementMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

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
    void testShowAllAd() {
        AdvertisementDTO ad1 = new AdvertisementDTO();
        AdvertisementDTO ad2 = new AdvertisementDTO();

        List<AdvertisementDTO> allAdvertisements = List.of(ad1, ad2);

        when(advertisementService.findAllAd()).thenReturn(allAdvertisements);
        when(advertisementService.findAllAd(Sort.by(Sort.Order.asc("dateAdded"))))
                .thenReturn(List.of(ad1, ad2));

        ResponseEntity<?> responseWithoutSorting = advertisementController.showAllAdvertisement(false);

        assertEquals(HttpStatus.OK, responseWithoutSorting.getStatusCode());
        assertEquals("All advertisements", ((Map<?, ?>) Objects.requireNonNull(responseWithoutSorting.getBody())).get("message"));
        assertEquals(allAdvertisements, ((Map<?, ?>) responseWithoutSorting.getBody()).get("data"));

        verify(advertisementService, times(1)).findAllAd();

        ResponseEntity<?> responseWithSorting = advertisementController.showAllAdvertisement(true);

        assertEquals(HttpStatus.OK, responseWithSorting.getStatusCode());
        assertEquals("All advertisements", ((Map<?, ?>) Objects.requireNonNull(responseWithSorting.getBody())).get("message"));
        assertEquals(allAdvertisements, ((Map<?, ?>) responseWithSorting.getBody()).get("data"));

        verify(advertisementService, times(1)).findAllAd(Sort.by(Sort.Order.asc("dateAdded")));
    }


    @Test
    void testGetAdvertisementById() {
        Advertisement adv = mock(Advertisement.class);
        AdvertisementDTO advertisementDTO = mock(AdvertisementDTO.class);

        AdvertisementService advertisementService = mock(AdvertisementService.class);
        when(advertisementService.getAdvertisement(anyLong())).thenReturn(adv);

        AdvertisementMapper advertisementMapper = mock(AdvertisementMapper.class);
        when(advertisementMapper.toDTO(adv)).thenReturn(advertisementDTO);

        AdvertisementController controller = new AdvertisementController(advertisementService, advertisementMapper);

        ResponseEntity<?> response = controller.getAdvertisementById(1L);

        verify(advertisementService, times(1)).getAdvertisement(1L);

        verify(advertisementMapper, times(1)).toDTO(adv);

        assertEquals(HttpStatus.OK, response.getStatusCode());
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