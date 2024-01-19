package com.sellbycar.marketplace.ad;

import com.sellbycar.marketplace.util.exception.RequestException;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface AdvertisementService {

    List<AdvertisementDTO> findAdvertisements(AdvertisementFilter filter, Sort sort, int page, int size);

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

    /**
     * Retrieves a list of AdvertisementDTO objects associated with the current user.
     * This method retrieves and returns a list of AdvertisementDTO objects that belong to the
     * authenticated user. The advertisements may include details such as title, description,
     * and other relevant information.
     *
     * @return A List of AdvertisementDTO objects representing advertisements associated with the user.
     * If there are no advertisements, an empty list is returned. The list is never null.
     * @throws RequestException If there is an error while retrieving the user's advertisements.
     */
    List<AdvertisementDTO> getUserAdvertisement();

    void enableAdvertisement(Long id);
    void disableAdvertisement(Long id);
}
