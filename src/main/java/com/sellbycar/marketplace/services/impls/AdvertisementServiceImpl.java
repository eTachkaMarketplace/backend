package com.sellbycar.marketplace.services.impls;

import com.sellbycar.marketplace.models.dto.AdvertisementDTO;
import com.sellbycar.marketplace.models.entities.Advertisement;
import com.sellbycar.marketplace.models.entities.Car;
import com.sellbycar.marketplace.models.entities.Image;
import com.sellbycar.marketplace.models.entities.User;
import com.sellbycar.marketplace.repositories.AdvertisementRepository;
import com.sellbycar.marketplace.services.AdvertisementService;
import com.sellbycar.marketplace.services.ImageService;
import com.sellbycar.marketplace.services.UserService;
import com.sellbycar.marketplace.utilities.exception.CustomUserException;
import com.sellbycar.marketplace.utilities.exception.FavoritesCarsNotFoundException;
import com.sellbycar.marketplace.utilities.exception.InvalidAccessException;
import com.sellbycar.marketplace.utilities.mapper.AdvertisementMapper;
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
        List<Advertisement> advertisements = advertisementRepository.findAll();
        return advertisements.stream()
                .map(advertisementMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<AdvertisementDTO> findAllAd(Sort sort)
    {
        List<Advertisement> advertisements = advertisementRepository.findAll(sort);
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
        throw new CustomUserException("ADv with ID " + id + " not found");
    }

    @Transactional
    public void createAdvertisement(AdvertisementDTO advertisementDTO, List<MultipartFile> files) throws IOException {

        Advertisement advertisement = advertisementMapper.toModel(advertisementDTO);
        User user = userService.getUserFromSecurityContextHolder();

        advertisement.setUser(user);
        advertisement.setDateAdded(new Date());

        if (!files.isEmpty()) {
            for (MultipartFile multipartFile : files) {
                Image image = imageService.createImage(multipartFile);
                advertisement.addImageToAdvertisement(image);
            }
        }

        advertisementRepository.save(advertisement);
    }

    @Transactional
    public Advertisement updateADv(AdvertisementDTO advertisementDTO, Long id) {
        User user = userService.getUserFromSecurityContextHolder();

        Advertisement existingAd = advertisementRepository.findById(id)
                .orElseThrow(() -> new CustomUserException("Ad with ID " + id + " not found"));

        if (user.getId().equals(existingAd.getUser().getId())) {
            existingAd.setName(advertisementDTO.getOwnerName());
            existingAd.setDescription(advertisementDTO.getDescription());
            existingAd.setPrice(advertisementDTO.getPrice());
            Car car = existingAd.getCar();
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
    public Set<Advertisement> getAllFavorites() {
        User user = userService.getUserFromSecurityContextHolder();
        Set<Advertisement> favCars = user.getFavoriteCars();

        if (!favCars.isEmpty()) {
            return favCars;
        } else {
            throw new FavoritesCarsNotFoundException("You did not add a cars to your favorite list");
        }
    }

    @Transactional
    public Advertisement addToFavoriteList(Long id) {
        User user = userService.getUserFromSecurityContextHolder();
        Advertisement advertisement = advertisementRepository.findById(id).orElse(null);

        if (advertisement != null) {

            // Get all favorites of current user
            Set<Advertisement> favoriteCarsOfUser = user.getFavoriteCars();

            // Adding the advertisement to set favorites of users
            favoriteCarsOfUser.add(advertisement);
            user.setFavoriteCars(favoriteCarsOfUser);
        } else
        {
            throw new FavoritesCarsNotFoundException(String.format(
                    "Advertisement with id %s was not found.", id
            ));
        }

        return advertisement;
    }

    @Transactional
    public void removeFromFavoriteList(Long id) {
        User user = userService.getUserFromSecurityContextHolder();

        // Get all favorites of current user
        Set<Advertisement> favoriteCarsOfUser = user.getFavoriteCars();

        Advertisement advertisement = advertisementRepository.findById(id).orElse(null);

        if (advertisement != null) {
            favoriteCarsOfUser.remove(advertisement);
            user.setFavoriteCars(favoriteCarsOfUser);
        }
    }

    @Transactional
    public void removeAdvertisement(Long id) {
        User existingUser = userService.getUserFromSecurityContextHolder();
        Optional<Advertisement> optionalAdv = Optional.of(advertisementRepository.findById(id).orElseThrow(
                () -> new CustomUserException("Advertisement not found")
        ));

        optionalAdv.ifPresent(advertisement -> {
            User user = advertisement.getUser();
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
