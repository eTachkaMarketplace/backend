package com.sellbycar.marketplace.repository;

import com.sellbycar.marketplace.repository.model.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdvertisementRepository extends JpaRepository<Advertisement,Long> {
}
