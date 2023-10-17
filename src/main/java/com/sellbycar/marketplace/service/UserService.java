package com.sellbycar.marketplace.service;

import com.sellbycar.marketplace.payload.request.SignupRequest;
import com.sellbycar.marketplace.repository.UserRepository;
import com.sellbycar.marketplace.repository.enums.UserRole;
import com.sellbycar.marketplace.repository.model.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetails {
    private final UserRepository userRepository;
    private User user;
    private final PasswordEncoder passwordEncoder;
    private final MailSenderService mailSenderService;
    private final HttpServletRequest httpServletRequest;


    public boolean createUser(SignupRequest signUpRequest) {
        String email = signUpRequest.getEmail();
        if (userRepository.findByEmail(email).isPresent()) return false;
        User user = new User();
        user.setEmail(email);
        user.setFirstName(signUpRequest.getUsername());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.getAuthority().add(UserRole.USER);
        user.setEnabled(true);
        userRepository.save(user);
        return true;
    }


    public boolean isEmailAlreadyExists(String email) {
        Optional<User> existingUser = userRepository.findByEmail(email);
        return existingUser.isPresent();
    }


//    Optional<User> user = userRepository.findById(id);
//        if(user.isPresent()) {
//        userRepository.deleteById(id);
//        return true;
//    }
//        return false;
//}

    public User getUser(long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public boolean deleteUser(long id) {
        userRepository.deleteById(id);
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getAuthority();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.getEnabled();
    }
}
