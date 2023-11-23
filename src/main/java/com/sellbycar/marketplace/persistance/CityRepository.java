package com.sellbycar.marketplace.persistance;

import com.sellbycar.marketplace.persistance.model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
}
