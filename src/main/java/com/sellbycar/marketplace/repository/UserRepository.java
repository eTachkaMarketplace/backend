package com.sellbycar.marketplace.repository;

import com.sellbycar.marketplace.repository.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByFirstName(String name);

    Optional<User> findByEmail(String email);

}
