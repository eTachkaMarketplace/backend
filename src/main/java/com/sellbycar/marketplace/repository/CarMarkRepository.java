package com.sellbycar.marketplace.repository;

import com.sellbycar.marketplace.repository.model.CarMark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarMarkRepository extends JpaRepository<CarMark, Long> {
}
