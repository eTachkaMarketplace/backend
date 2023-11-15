package com.sellbycar.marketplace.service.impl;

import com.sellbycar.marketplace.repository.UserRepository;
import com.sellbycar.marketplace.repository.enums.UserRole;
import com.sellbycar.marketplace.repository.model.User;
import com.sellbycar.marketplace.rest.exception.UserDataException;
import com.sellbycar.marketplace.rest.payload.request.SignupRequest;
import com.sellbycar.marketplace.service.MailSenderService;
import com.sellbycar.marketplace.service.UserService;
import com.sellbycar.marketplace.service.impl.UserDetailsServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final PasswordEncoder passwordEncoder;
    private final MailSenderService mailSenderService;
    private final HttpServletRequest httpServletRequest;

    public boolean createNewUser(SignupRequest signUpRequest) {
        String username = signUpRequest.getUsername();
        String password = signUpRequest.getPassword();
        String email = signUpRequest.getEmail();
        if (username == null || username.length() < 2 || containsDigits(username))
            throw new UserDataException("Invalid username. Usernames should be at least 2 symbols long and should not contain digits.");
        if (password == null || password.length() < 5 || !isPasswordValid(password))
            throw new UserDataException("The password must meet the following criteria:\n"
                    + "- At least 5 characters long\n"
                    + "- Must contain at least one uppercase letter\n"
                    + "- Must contain at least one lowercase letter\n"
                    + "- Must contain at least one digit\n"
                    + "- Must not contain Cyrillic characters");
        if (email == null || email.isEmpty() || !isEmailValid(email)) {
            throw new UserDataException("Invalid email address. Email should not be empty and should have a valid format.");
        }


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

    private boolean containsDigits(String str) {
        for (char c : str.toCharArray()) {
            if (Character.isDigit(c)) {
                return true;
            }
        }
        return false;
    }

    private boolean isPhoneNumberValid(String phoneNumber) {
        return phoneNumber.matches("\\d{10,}");
    }

    private boolean isEmailValid(String email) {
        String emailRegex = "^[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isPasswordValid(String password) {
        String passRegex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)[A-Za-z\\d]+$";
        Pattern pattern = Pattern.compile(passRegex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }


    public boolean isEmailAlreadyExists(String email) {
        Optional<User> existingUser = userRepository.findByEmail(email);
        return existingUser.isPresent();
    }

    public User existByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }


    public User findUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public boolean deleteUser(long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Authentication userAuthentication(User user) {
        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(user.getEmail());
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

}
