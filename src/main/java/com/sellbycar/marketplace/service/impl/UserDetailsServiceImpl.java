package com.sellbycar.marketplace.service.impl;


import com.sellbycar.marketplace.config.SecurityUserDetailsConfig;
import com.sellbycar.marketplace.repository.UserRepository;
import com.sellbycar.marketplace.repository.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) throw new UsernameNotFoundException("User with this email not found");
        return new SecurityUserDetailsConfig(optionalUser.get());
    }
}
