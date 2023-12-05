package com.sellbycar.marketplace.services;

import com.sellbycar.marketplace.models.dto.AdvertisementDTO;
import com.sellbycar.marketplace.models.entities.Advertisement;

import java.util.List;
import java.util.Set;

public interface AdvertisementService
{
    /**
     * Find all advertisements in Marketplace
     *
     * @return List of all advertisements
     */
    List<AdvertisementDTO> findAllAd();

    /**
     * Find specifiv advertisement by id in Marketplace
     *
     * @param id id of user in order to get it
     * @return Advertisement object representing the user if found, null otherwise.
     */
    Advertisement getAd(Long id);

    /**
     * Create new advertisement
     *
     * @param advertisementDTO object with datas for creating
     */
    void saveNewAd(AdvertisementDTO advertisementDTO);

    /**
     * Update thr advertisement
     *
     * @param advertisementDTO object with datas for changes
     * @param id id of advertisement in order to get it
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

}
