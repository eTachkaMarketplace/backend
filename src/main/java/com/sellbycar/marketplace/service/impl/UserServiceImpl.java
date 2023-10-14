package com.sellbycar.marketplace.service.impl;

import com.sellbycar.marketplace.repository.enums.UserRole;
import com.sellbycar.marketplace.repository.model.User;
import com.sellbycar.marketplace.repository.UserRepository;
import com.sellbycar.marketplace.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public User getUser(long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }

    @Override
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        user.getAuthority().add(UserRole.USER);
        return userRepository.save(user);
    }


    @Override
    public boolean isEmailAlreadyExists(String email) {
        Optional<User> existingUser = userRepository.findByEmail(email);
        return existingUser.isPresent();
    }


    @Override
    public boolean deleteUser(long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

}
