package com.sellbycar.marketplace.persistance;

import com.sellbycar.marketplace.persistance.model.CarMark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarMarkRepository extends JpaRepository<CarMark, Long> {
}
