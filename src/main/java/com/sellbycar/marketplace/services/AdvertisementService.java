package com.sellbycar.marketplace.services;

import com.sellbycar.marketplace.models.dto.AdvertisementDTO;
import com.sellbycar.marketplace.models.entities.Advertisement;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface AdvertisementService {
    /**
     * Finds a user in the database by checking the given user ID and converts it into DTO.
     *
     * @return List of all advertisements
     */
    List<AdvertisementDTO> findAllAd();

    List<AdvertisementDTO> findAllAd(Sort sort);

    /**
     * Finds a user in the database by checking the given user ID and converts it into DTO.
     *
     * @param id id of user in order to get it
     * @return Advertisement object representing the user if found, null otherwise.
     */
    Advertisement getAdvertisement(Long id);

    /**
     * Finds a user in the database by checking the given user ID and converts it into DTO.
     *
     * @param advertisementDTO object with datas for creating
     * @return id of advertisement
     */
    long createAdvertisement(AdvertisementDTO advertisementDTO, List<MultipartFile> files) throws IOException;

    /**
     * Finds a user in the database by checking the given user ID and converts it into DTO.
     *
     * @param advertisementDTO object with datas for changes
     * @param id               id of advertisement in order to get it
     */
    Advertisement updateADv(AdvertisementDTO advertisementDTO, Long id);

    /**
     * Get all advertisements from favorite list
     *
     * @return Set of all advertisements in favorite list
     */
    Set<Advertisement> getAllFavorites();

    /**
     * add the advertisement to favorite list
     *
     * @param id id of advertisement in order to get it
     */
    Advertisement addToFavoriteList(Long id);

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

    /**
     * Finds all user's advertisements in the database
     *
     * @return All user's advertisements in list
     */
    List<AdvertisementDTO> findUserAdvertisement();

}
