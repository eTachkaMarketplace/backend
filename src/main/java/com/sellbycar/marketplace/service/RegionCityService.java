package com.sellbycar.marketplace.service;

import com.sellbycar.marketplace.repository.CityRepository;
import com.sellbycar.marketplace.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegionCityService {
    private final RegionRepository regionRepository;
    private final CityRepository cityRepository;


}
