package com.sellbycar.marketplace.repositories;

import com.sellbycar.marketplace.models.entities.Region;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionRepository extends JpaRepository<Region, Long> {
}
