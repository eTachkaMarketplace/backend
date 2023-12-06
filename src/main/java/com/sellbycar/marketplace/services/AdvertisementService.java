package com.sellbycar.marketplace.services;

import com.sellbycar.marketplace.models.dto.AdvertisementDTO;
import com.sellbycar.marketplace.models.entities.Advertisement;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AdvertisementService {
    /**
     * Finds a user in the database by checking the given user ID and converts it into DTO.
     *
     * @return List of all advertisements
     */
    List<AdvertisementDTO> findAllAd();

    /**
     * Finds a user in the database by checking the given user ID and converts it into DTO.
     *
     * @param id id of user in order to get it
     * @return Advertisement object representing the user if found, null otherwise.
     */
    Advertisement getAd(Long id);

    /**
     * Finds a user in the database by checking the given user ID and converts it into DTO.
     *
     * @param advertisementDTO object with datas for creating
     */
    public void createAdvertisement(AdvertisementDTO advertisementDTO, List<MultipartFile> files) throws IOException;

        /**
         * Finds a user in the database by checking the given user ID and converts it into DTO.
         *
         * @param advertisementDTO object with datas for changes
         * @param id id of advertisement in order to get it
         */
        Advertisement updateADv (AdvertisementDTO advertisementDTO, Long id);

    }
