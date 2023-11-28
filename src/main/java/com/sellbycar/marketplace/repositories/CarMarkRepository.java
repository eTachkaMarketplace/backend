package com.sellbycar.marketplace.repositories;

import com.sellbycar.marketplace.models.entities.CarMark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarMarkRepository extends JpaRepository<CarMark, Long> {
}
