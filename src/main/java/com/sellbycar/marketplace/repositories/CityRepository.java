package com.sellbycar.marketplace.repositories;

import com.sellbycar.marketplace.models.entities.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
}