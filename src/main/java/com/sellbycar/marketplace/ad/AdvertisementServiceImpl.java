package com.sellbycar.marketplace.ad;

import com.sellbycar.marketplace.car.CarDAO;
import com.sellbycar.marketplace.image.ImageDAO;
import com.sellbycar.marketplace.image.ImageService;
import com.sellbycar.marketplace.user.UserDAO;
import com.sellbycar.marketplace.user.UserService;
import com.sellbycar.marketplace.util.exception.CustomUserException;
import com.sellbycar.marketplace.util.exception.FavoritesCarsNotFoundException;
import com.sellbycar.marketplace.util.exception.InvalidAccessException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
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
    private final ImageService imageService;

    @Transactional
    public List<AdvertisementDTO> findAllAd() {
        List<AdvertisementDAO> advertisements = advertisementRepository.findAll();
        return advertisements.stream()
                .map(advertisementMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<AdvertisementDTO> findAllAd(Sort sort) {
        List<AdvertisementDAO> advertisements = advertisementRepository.findAll(sort);
        return advertisements.stream()
                .map(advertisementMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public AdvertisementDAO getAdvertisement(Long id) {
        Optional<AdvertisementDAO> ad = advertisementRepository.findById(id);
        if (ad.isPresent()) {
            return ad.get();
        }
        throw new CustomUserException("ADv with ID " + id + " not found");
    }

    @Transactional
    public long createAdvertisement(AdvertisementDTO advertisementDTO, List<MultipartFile> files) throws IOException {

        AdvertisementDAO advertisement = advertisementMapper.toModel(advertisementDTO);
        UserDAO user = userService.getUserFromSecurityContextHolder();

        advertisement.setUser(user);
        advertisement.setDateAdded(new Date());

        if (!files.isEmpty()) {
            for (MultipartFile multipartFile : files) {
                ImageDAO image = imageService.createImage(multipartFile);
                advertisement.addImageToAdvertisement(image);
            }
        }

        advertisementRepository.save(advertisement);
        AdvertisementDTO createdAdvertisement = advertisementMapper.toDTO(advertisement);
        return createdAdvertisement.getId();
    }

    @Transactional
    public AdvertisementDAO updateADv(AdvertisementDTO advertisementDTO, Long id) {
        UserDAO user = userService.getUserFromSecurityContextHolder();

        AdvertisementDAO existingAd = advertisementRepository.findById(id)
                .orElseThrow(() -> new CustomUserException("Ad with ID " + id + " not found"));

        if (user.getId().equals(existingAd.getUser().getId())) {
            existingAd.setName(advertisementDTO.getOwnerName());
            existingAd.setDescription(advertisementDTO.getDescription());
            existingAd.setPrice(advertisementDTO.getPrice());
            CarDAO car = existingAd.getCar();
            car.setYearToCreate(advertisementDTO.getCarDTO().getYearToCreate());
            car.setCarNumber(advertisementDTO.getCarDTO().getCarNumber());
            car.setVinNumber(advertisementDTO.getCarDTO().getVinNumber());
            car.setMileage(advertisementDTO.getCarDTO().getMileage());
            existingAd.setCar(car);

            advertisementRepository.save(existingAd);
            return existingAd;
        } else {
            throw new InvalidAccessException("You don't have permission to update this ad");
        }
    }

    @Transactional
    public Set<AdvertisementDAO> getAllFavorites() {
        UserDAO user = userService.getUserFromSecurityContextHolder();
        Set<AdvertisementDAO> favCars = user.getFavoriteCars();

        if (!favCars.isEmpty()) {
            return favCars;
        } else {
            throw new FavoritesCarsNotFoundException("You did not add a cars to your favorite list");
        }
    }

    @Transactional
    public AdvertisementDAO addToFavoriteList(Long id) {
        UserDAO user = userService.getUserFromSecurityContextHolder();
        AdvertisementDAO advertisement = advertisementRepository.findById(id).orElse(null);

        if (advertisement != null) {

            // Get all favorites of current user
            Set<AdvertisementDAO> favoriteCarsOfUser = user.getFavoriteCars();

            // Adding the advertisement to set favorites of users
            favoriteCarsOfUser.add(advertisement);
            user.setFavoriteCars(favoriteCarsOfUser);
        } else {
            throw new FavoritesCarsNotFoundException(String.format(
                    "Advertisement with id %s was not found.", id
            ));
        }

        return advertisement;
    }

    @Transactional
    public void removeFromFavoriteList(Long id) {
        UserDAO user = userService.getUserFromSecurityContextHolder();

        // Get all favorites of current user
        Set<AdvertisementDAO> favoriteCarsOfUser = user.getFavoriteCars();

        AdvertisementDAO advertisement = advertisementRepository.findById(id).orElse(null);

        if (advertisement != null) {
            favoriteCarsOfUser.remove(advertisement);
            user.setFavoriteCars(favoriteCarsOfUser);
        }
    }

    @Transactional
    public void removeAdvertisement(Long id) {
        UserDAO existingUser = userService.getUserFromSecurityContextHolder();
        Optional<AdvertisementDAO> optionalAdv = Optional.of(advertisementRepository.findById(id).orElseThrow(
                () -> new CustomUserException("Advertisement not found")
        ));

        optionalAdv.ifPresent(advertisement -> {
            UserDAO user = advertisement.getUser();
            if (existingUser.equals(user)) {
                user.getAdvertisement().remove(advertisement);
                advertisement.setUser(null);
                advertisementRepository.delete(advertisement);
            } else {
                throw new InvalidAccessException("Access denied");
            }
        });
    }

}
