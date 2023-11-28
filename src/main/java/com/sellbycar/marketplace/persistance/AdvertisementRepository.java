package com.sellbycar.marketplace.persistance;

import com.sellbycar.marketplace.persistance.model.Advertisement;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {

    @NonNull List<Advertisement> findAll();
}
