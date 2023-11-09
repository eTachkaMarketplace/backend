package com.sellbycar.marketplace.repository;

import com.sellbycar.marketplace.repository.model.CarMark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarMarkRepository extends JpaRepository<CarMark, Long> {
}
