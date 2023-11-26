package com.sellbycar.marketplace.service;

import com.sellbycar.marketplace.persistance.AdvertisementRepository;
import com.sellbycar.marketplace.persistance.model.Advertisement;
import com.sellbycar.marketplace.persistance.model.Car;
import com.sellbycar.marketplace.persistance.model.User;
import com.sellbycar.marketplace.rest.dto.AdvertisementDTO;
import com.sellbycar.marketplace.rest.exception.UserDataException;
import com.sellbycar.marketplace.rest.mapper.AdvertisementMapper;
import com.sellbycar.marketplace.rest.mapper.CarMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
@RequiredArgsConstructor
public class AdvertisementService {

    private final AdvertisementRepository advertisementRepository;

    private final UserService userService;
    private final AdvertisementMapper advertisementMapper;
    private final CarMapper carMapper;

    @Transactional
    public List<AdvertisementDTO> findAllAd() {
        List<Advertisement> advertisements = advertisementRepository.findAll();
        return advertisements.stream()
                .map(advertisementMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public AdvertisementDTO getAd(Long id) {
        Optional<Advertisement> ad = advertisementRepository.findById(id);
        if (ad.isPresent()) {
            return advertisementMapper.toDTO(ad.get());
        }
        //TODO
        throw new UserDataException("No ADv id in database");
    }

    @Transactional
    public void saveNewAd(AdvertisementDTO advertisementDTO) {
        Advertisement advertisement = advertisementMapper.toModel(advertisementDTO);
//        Car car = carMapper.toModel(advertisementDTO.getCarDTO());
//        advertisement.setCar(car);
        User user = userService.getUserFromSecurityContextHolder();
        advertisement.setUser(user);
        advertisementRepository.save(advertisement);
    }
}
