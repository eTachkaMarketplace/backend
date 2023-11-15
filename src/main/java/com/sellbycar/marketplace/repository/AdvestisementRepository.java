package com.sellbycar.marketplace.repository;

import com.sellbycar.marketplace.repository.model.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdvestisementRepository extends JpaRepository<Advertisement,Long> {
}
