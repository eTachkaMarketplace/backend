package com.sellbycar.marketplace.image;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<ImageDAO, Long> {
    Optional<ImageDAO> findById(Long id);
}
