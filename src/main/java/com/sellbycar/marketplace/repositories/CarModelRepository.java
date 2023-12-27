package com.sellbycar.marketplace.repositories;

import com.sellbycar.marketplace.models.entities.CarModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarModelRepository extends JpaRepository<CarModel, Long> {
}
