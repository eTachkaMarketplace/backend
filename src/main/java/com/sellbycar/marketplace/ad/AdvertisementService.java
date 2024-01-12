package com.sellbycar.marketplace.ad;

import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface AdvertisementService {

    List<AdvertisementDTO> findAdvertisements();

    List<AdvertisementDTO> findAdvertisements(Sort sort);

    /**
     * Finds a user in the database by checking the given user ID and converts it into DTO.
     *
     * @param id id of user in order to get it
     * @return Advertisement object representing the user if found, null otherwise.
     */
    AdvertisementDAO getAdvertisement(Long id);

    AdvertisementDAO createAdvertisement(AdvertisementDTO advertisementDTO, List<MultipartFile> files) throws IOException;

    /**
     * Finds a user in the database by checking the given user ID and converts it into DTO.
     *
     * @param advertisementDTO object with datas for changes
     */
    AdvertisementDAO updateAdvertisement(AdvertisementDTO advertisementDTO);

    /**
     * Get all advertisements from favorite list
     *
     * @return Set of all advertisements in favorite list
     */
    Set<AdvertisementDAO> getAllFavorites();

    /**
     * add the advertisement to favorite list
     *
     * @param id id of advertisement in order to get it
     */
    AdvertisementDAO addToFavoriteList(Long id);

    /**
     * remove the advertisement from favorite list
     *
     * @param id id of advertisement in order to get it
     */
    void removeFromFavoriteList(Long id);

    /**
     * Removes the specified advertisement from the system.
     *
     * @param id The identifier of the advertisement to be removed.
     */
    void removeAdvertisement(Long id);
}
