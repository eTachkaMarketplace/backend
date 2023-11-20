package com.sellbycar.marketplace.service;

import com.sellbycar.marketplace.repository.AdvertisementRepository;
import com.sellbycar.marketplace.repository.model.Advertisement;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdvertisementService {

    private final AdvertisementRepository advertisementRepository;

    public void save(Advertisement advertisement) {
        advertisementRepository.save(advertisement);
    }

}
