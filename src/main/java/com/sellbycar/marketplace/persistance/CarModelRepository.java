package com.sellbycar.marketplace.persistance;

import com.sellbycar.marketplace.persistance.model.CarModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarModelRepository extends JpaRepository<CarModel, Long> {
}
