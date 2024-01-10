package com.sellbycar.marketplace.ad;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdvertisementRepository extends JpaRepository<AdvertisementDAO, Long> {

    @NonNull List<AdvertisementDAO> findAll();
}
