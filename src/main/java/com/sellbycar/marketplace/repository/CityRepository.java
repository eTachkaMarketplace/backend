package com.sellbycar.marketplace.repository;

import com.sellbycar.marketplace.repository.model.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City, Long> {
}
