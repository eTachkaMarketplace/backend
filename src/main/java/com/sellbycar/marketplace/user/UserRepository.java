package com.sellbycar.marketplace.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserDAO, Long> {

    Optional<UserDAO> findByEmail(String email);

    Optional<UserDAO> findByUniqueCode(String uniqueCode);
}
