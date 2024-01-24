package com.sellbycar.marketplace.ad;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface AdvertisementRepository extends JpaRepository<AdvertisementDAO, Long>, JpaSpecificationExecutor<AdvertisementDAO> {

    @NonNull List<AdvertisementDAO> findAll();

    @NonNull List<AdvertisementDAO> findByUserId(Long userId);
}
