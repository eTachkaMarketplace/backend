package com.sellbycar.marketplace.repository;

import com.sellbycar.marketplace.repository.model.CarModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarModelRepository extends JpaRepository<CarModel, Long> {
}
