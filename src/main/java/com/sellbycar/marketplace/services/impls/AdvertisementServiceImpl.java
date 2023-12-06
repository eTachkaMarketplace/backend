package com.sellbycar.marketplace.services.impls;

import com.sellbycar.marketplace.models.dto.AdvertisementDTO;
import com.sellbycar.marketplace.models.entities.Advertisement;
import com.sellbycar.marketplace.models.entities.Car;
import com.sellbycar.marketplace.models.entities.Image;
import com.sellbycar.marketplace.models.entities.User;
import com.sellbycar.marketplace.repositories.AdvertisementRepository;
import com.sellbycar.marketplace.services.AdvertisementService;
import com.sellbycar.marketplace.services.UserService;
import com.sellbycar.marketplace.utilities.exception.FavoritesCarsNotFoundException;
import com.sellbycar.marketplace.utilities.exception.UserDataException;
import com.sellbycar.marketplace.utilities.mapper.AdvertisementMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdvertisementServiceImpl implements AdvertisementService {
    private final AdvertisementRepository advertisementRepository;
    private final UserService userService;
    private final AdvertisementMapper advertisementMapper;

    @Transactional
    public List<AdvertisementDTO> findAllAd() {
        List<Advertisement> advertisements = advertisementRepository.findAll();
        return advertisements.stream()
                .map(advertisementMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public Advertisement getAd(Long id) {
        Optional<Advertisement> ad = advertisementRepository.findById(id);
        if (ad.isPresent()) {
            return ad.get();
        }
        //TODO
        throw new UserDataException("ADv with ID " + id + " not found");
    }

    @Transactional
    public void createAdvertisement(AdvertisementDTO advertisementDTO, List<MultipartFile> files) throws IOException {
        Image image2;
        Image image3;

        Advertisement advertisement = advertisementMapper.toModel(advertisementDTO);
        User user = userService.getUserFromSecurityContextHolder();
        advertisement.setUser(user);
//        if (files.get(0).getSize() != 0) {
//            image1 = toImageEntity(files.get(0));
//            image1.setPreviewImage(true);
//            advertisement.addImageToAdvertisement(image1);
//        }
        if (!files.isEmpty()) {
            for (MultipartFile multipartFile : files) {
                Image image = toImageEntity(multipartFile);
                image.setPreviewImage(true);
                advertisement.addImageToAdvertisement(image);
            }
        }
//        if (file3.getSize() != 0) {
//            image3 = toImageEntity(file3);
//            advertisement.addImageToAdvertisement(image3);
//        }
        log.info("Saving new Advertisement. Title: {}; Author: {}");
        Advertisement advertisementFromDB = advertisementRepository.save(advertisement);
        advertisementFromDB.setPreviewImageId(advertisementFromDB.getImages().get(0).getId());
        advertisementRepository.save(advertisement);
    }

    private Image toImageEntity(MultipartFile file1) throws IOException {
        Image image = new Image();
        image.setName(file1.getName());
        image.setContentType(file1.getContentType());
        image.setSize(file1.getSize());
        image.setResource(file1.getBytes());
        return image;
    }

    public Advertisement updateADv(AdvertisementDTO advertisementDTO, Long id) {
        User user = userService.getUserFromSecurityContextHolder();

        Advertisement existingAd = advertisementRepository.findById(id)
                .orElseThrow(() -> new UserDataException("Ad with ID " + id + " not found"));

        if (user.getId().equals(existingAd.getUser().getId())) {
            existingAd.setName(advertisementDTO.getName());
            existingAd.setDescription(advertisementDTO.getDescription());
            existingAd.setPrice(advertisementDTO.getPrice());
            existingAd.setChange(advertisementDTO.isChange());
            existingAd.setBargain(advertisementDTO.isBargain());
            existingAd.setCrashed(advertisementDTO.isCrashed());
            Car car = existingAd.getCar();
            car.setYearToCreate(advertisementDTO.getCarDTO().getYearToCreate());
            car.setCarNumber(advertisementDTO.getCarDTO().getCarNumber());
            car.setVinNumber(advertisementDTO.getCarDTO().getVinNumber());
            car.setMileage(advertisementDTO.getCarDTO().getMileage());
            existingAd.setCar(car);

            advertisementRepository.save(existingAd);
            return existingAd;
        } else {
            throw new UserDataException("You don't have permission to update this ad");
        }
    }

    @Transactional
    public Set<Advertisement> getAllFavorites()
    {
        User user = userService.getUserFromSecurityContextHolder();
        Set<Advertisement> favCars = user.getFavoriteCars();

        if (!favCars.isEmpty())
        {
            return favCars;
        }
        else
        {
            throw new FavoritesCarsNotFoundException();
        }
    }

    @Transactional
    public Advertisement addToFavoriteList(Long id) {
        User user = userService.getUserFromSecurityContextHolder();
        Advertisement advertisement = advertisementRepository.findById(id).orElse(null);
        Set<Advertisement> favoriteCarsOfUser = getAllFavorites();
        favoriteCarsOfUser.add(advertisement);

        if (advertisement != null) {
            user.setFavoriteCars(favoriteCarsOfUser);
        }

        return advertisement;
    }

    @Transactional
    public void removeFromFavoriteList(Long id) {
        User user = userService.getUserFromSecurityContextHolder();
        Set<Advertisement> favoriteCarsOfUser = getAllFavorites();
        Advertisement advertisement = advertisementRepository.findById(id).orElse(null);

        favoriteCarsOfUser.remove(advertisement);

        user.setFavoriteCars(favoriteCarsOfUser);
    }
}
