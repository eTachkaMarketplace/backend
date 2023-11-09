package com.sellbycar.marketplace.repository;

import com.sellbycar.marketplace.repository.model.Region;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionRepository extends JpaRepository<Region, Long> {
}
