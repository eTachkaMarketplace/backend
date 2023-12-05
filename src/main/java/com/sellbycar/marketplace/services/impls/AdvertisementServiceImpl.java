package com.sellbycar.marketplace.services.impls;

import com.sellbycar.marketplace.models.dto.AdvertisementDTO;
import com.sellbycar.marketplace.models.entities.Advertisement;
import com.sellbycar.marketplace.models.entities.Car;
import com.sellbycar.marketplace.models.entities.User;
import com.sellbycar.marketplace.repositories.AdvertisementRepository;
import com.sellbycar.marketplace.repositories.UserRepository;
import com.sellbycar.marketplace.services.AdvertisementService;
import com.sellbycar.marketplace.services.UserService;
import com.sellbycar.marketplace.utilities.exception.FavoritesCarsNotFoundException;
import com.sellbycar.marketplace.utilities.exception.UserDataException;
import com.sellbycar.marketplace.utilities.mapper.AdvertisementMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdvertisementServiceImpl implements AdvertisementService
{
    private final AdvertisementRepository advertisementRepository;
    private final UserRepository userRepository;
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
    public void saveNewAd(AdvertisementDTO advertisementDTO) {
        Advertisement advertisement = advertisementMapper.toModel(advertisementDTO);
        User user = userService.getUserFromSecurityContextHolder();
        advertisement.setUser(user);
        advertisementRepository.save(advertisement);
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
