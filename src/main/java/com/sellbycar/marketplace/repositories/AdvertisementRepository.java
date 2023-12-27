package com.sellbycar.marketplace.repositories;

import com.sellbycar.marketplace.models.entities.Advertisement;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {

    @NonNull List<Advertisement> findAll();
}
