package com.sellbycar.marketplace.ad;

import com.sellbycar.marketplace.car.CarDAO;
import com.sellbycar.marketplace.car.CarDTO;
import com.sellbycar.marketplace.image.ImageDAO;
import com.sellbycar.marketplace.image.ImageService;
import com.sellbycar.marketplace.user.UserDAO;
import com.sellbycar.marketplace.user.UserRepository;
import com.sellbycar.marketplace.user.UserService;
import com.sellbycar.marketplace.util.exception.RequestException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.ArrayList;
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
    private final UserRepository userRepository;

    @Override
    public List<AdvertisementDTO> findAdvertisements(AdvertisementFilter filter, Sort sort, int page, int size) {
        Pageable pageable = PageRequest.of(page, Integer.min(size, 100), sort);
        return advertisementRepository.findAll(filter.toSpecification(), pageable)
                .map(advertisementMapper::toDTO)
                .stream()
                .toList();
    }

    @Transactional
    public AdvertisementDAO getAdvertisement(Long id) {
        Optional<AdvertisementDAO> advertisement = advertisementRepository.findById(id);
        if (advertisement.isEmpty()) {
            throw RequestException.notFound("Advertisement does not exist.");
        }
        return advertisement.get();
    }

    @Transactional
    public AdvertisementDAO createAdvertisement(AdvertisementDTO advertisementDTO, List<MultipartFile> files) {
        UserDAO user = userService.getUserFromSecurityContextHolder();
        AdvertisementDAO advertisement = advertisementMapper.toDAO(advertisementDTO);
        advertisement.setUser(user);
        advertisement.setCreatedTimestamp(Instant.now());
        List<ImageDAO> images = new ArrayList<>();
        for (MultipartFile file : files) {
            images.add(imageService.createImage(file));
        }
        advertisement.setImages(images);
        advertisement.setPreviewImage(images.get(0));
        return advertisementRepository.save(advertisement);
    }

    @Transactional
    public AdvertisementDAO updateAdvertisement(AdvertisementDTO updated) {
        UserDAO user = userService.getUserFromSecurityContextHolder();
        Long id = updated.getId();
        AdvertisementDAO current = advertisementRepository.findById(id)
                .orElseThrow(() -> RequestException.notFound("Advertisement does not exist."));
        boolean isUpdatedByOwner = user.getId().equals(current.getUser().getId());
        if (!isUpdatedByOwner) {
            throw RequestException.forbidden("You do not have authority to update this advertisement.");
        }
        Optional.ofNullable(updated.getContactName()).ifPresent(current::setContactName);
        Optional.ofNullable(updated.getContactPhone()).ifPresent(current::setContactPhone);
        Optional.ofNullable(updated.getDescription()).ifPresent(current::setDescription);
        Optional.ofNullable(updated.getRegion()).ifPresent(current::setRegion);

        CarDAO car = current.getCar();
        CarDTO updatedCar = updated.getCar();
        if (updatedCar != null) {
            Optional.ofNullable(updatedCar.getBrand()).ifPresent(car::setBrand);
            Optional.ofNullable(updatedCar.getModel()).ifPresent(car::setModel);
            Optional.ofNullable(updatedCar.getYear()).ifPresent(car::setYear);
            Optional.ofNullable(updatedCar.getColor()).ifPresent(car::setColor);
            Optional.ofNullable(updatedCar.getLicensePlate()).ifPresent(car::setLicensePlate);
            Optional.ofNullable(updatedCar.getVin()).ifPresent(car::setVin);
            Optional.ofNullable(updatedCar.getMileage()).ifPresent(car::setMileage);
            Optional.ofNullable(updatedCar.getPrice()).ifPresent(car::setPrice);
            Optional.ofNullable(updatedCar.getTransmissionType()).ifPresent(car::setTransmissionType);
            Optional.ofNullable(updatedCar.getTechnicalState()).ifPresent(car::setTechnicalState);
            Optional.ofNullable(updatedCar.getEngineType()).ifPresent(car::setEngineType);
            Optional.ofNullable(updatedCar.getEngineVolume()).ifPresent(car::setEngineVolume);
            Optional.ofNullable(updatedCar.getBodyType()).ifPresent(car::setBodyType);
            Optional.ofNullable(updatedCar.getDriveType()).ifPresent(car::setDriveType);
        }
        advertisementRepository.save(current);
        return current;
    }

    @Transactional
    public Set<AdvertisementDAO> getAllFavorites() {
        UserDAO user = userService.getUserFromSecurityContextHolder();
        return user.getFavorites();
    }

    @Transactional
    public AdvertisementDAO addToFavoriteList(Long id) {
        UserDAO user = userService.getUserFromSecurityContextHolder();
        AdvertisementDAO advertisement = advertisementRepository.findById(id)
                .orElseThrow(() -> RequestException.notFound("Advertisement does not exist."));
        user.addFavorite(advertisement);
        userRepository.save(user);
        return advertisement;
    }

    @Transactional
    public void removeFromFavoriteList(Long id) {
        UserDAO user = userService.getUserFromSecurityContextHolder();
        AdvertisementDAO advertisement = advertisementRepository.findById(id)
                .orElseThrow(() -> RequestException.notFound("Advertisement does not exist."));
        user.removeFavorite(advertisement);
        userRepository.save(user);
    }

    @Transactional
    public void removeAdvertisement(Long id) {
        UserDAO existingUser = userService.getUserFromSecurityContextHolder();
        AdvertisementDAO advertisement = advertisementRepository.findById(id).orElse(null);
        if (advertisement == null) {
            throw RequestException.notFound("Advertisement does not exist.");
        }
        UserDAO user = advertisement.getUser();
        if (!existingUser.getId().equals(user.getId())) {
            throw RequestException.forbidden("You do not have authority to delete this advertisement.");
        }
        advertisementRepository.delete(advertisement);
    }

    @Transactional
    public List<AdvertisementDTO> getUserAdvertisement() {
        UserDAO user = userService.getUserFromSecurityContextHolder();
        List<AdvertisementDAO> advertisements = advertisementRepository.findAll();
        if (advertisements.stream().anyMatch(adv -> adv.getUser().getId().equals(user.getId()))) {
            return advertisements.stream()
                    .map(advertisementMapper::toDTO)
                    .collect(Collectors.toList());
        } else {
            throw new RequestException("Insufficient privileges", HttpStatus.FORBIDDEN);
        }
    }
}
