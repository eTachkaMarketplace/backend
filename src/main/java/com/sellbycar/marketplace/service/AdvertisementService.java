package com.sellbycar.marketplace.service;

import com.sellbycar.marketplace.persistance.AdvertisementRepository;
import com.sellbycar.marketplace.persistance.model.Advertisement;
import com.sellbycar.marketplace.persistance.model.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdvertisementService {

    private final AdvertisementRepository advertisementRepository;

    private final UserService userService;

    @Transactional
    public List<Advertisement> findAllAd() {
        return advertisementRepository.findAll();
    }

    @Transactional
    public Advertisement getAd(Long id) {
        Optional<Advertisement> ad = advertisementRepository.findById(id);
        if (ad.isPresent()) {
            return ad.get();
        }
        //TODO
        throw new IllegalStateException("Bad credentials for Ad");
    }

    @Transactional
    public void saveNewAd(Advertisement advertisement) {
//        advertisement.setAuto(new Auto());
        User user = userService.getUserFromSecurityContextHolder();
        advertisement.setUser(user);
        advertisementRepository.save(advertisement);
    }

}
