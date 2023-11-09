package com.sellbycar.marketplace.service;

import com.sellbycar.marketplace.payload.request.SignupRequest;
import com.sellbycar.marketplace.repository.UserRepository;
import com.sellbycar.marketplace.repository.enums.UserRole;
import com.sellbycar.marketplace.repository.model.User;
import com.sellbycar.marketplace.rest.exception.UserInValidDataException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetails {
    private final UserRepository userRepository;
    private User user;
    private final PasswordEncoder passwordEncoder;
    private final MailSenderService mailSenderService;
    private final HttpServletRequest httpServletRequest;


    public boolean createUser(SignupRequest signUpRequest) {
        String username = signUpRequest.getUsername();
        String password = signUpRequest.getPassword();
        String email = signUpRequest.getEmail();
        String phone = signUpRequest.getPhone();
        if (username == null || username.length() < 2 || containsDigits(username))
            throw new UserInValidDataException("Invalid username. Usernames should be at least 2 symbols long and should not contain digits.");
        if (password == null || password.length() < 5 || !isPasswordValid(password))
            throw new UserInValidDataException("The password must meet the following criteria:\n"
                    + "- At least 5 characters long\n"
                    + "- Must contain at least one uppercase letter\n"
                    + "- Must contain at least one lowercase letter\n"
                    + "- Must contain at least one digit\n"
//                    + "- Must contain at least one of the following special characters: @$^#!%*?&()\n"
                    + "- Must not contain Cyrillic characters");
        if (phone == null || phone.length() < 10 || !isPhoneNumberValid(phone)) {
            throw new UserInValidDataException("Invalid phone number. Phone numbers should be at least 10 digits long and contain only digits.");
        }
        if (email == null || email.isEmpty() || !isEmailValid(email)) {
            throw new UserInValidDataException("Invalid email address. Email should not be empty and should have a valid format.");
        }


        if (userRepository.findByEmail(email).isPresent()) return false;
        User user = new User();
        user.setEmail(email);
        user.setFirstName(signUpRequest.getUsername());
        user.setPhone(signUpRequest.getPhone());
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
//        String passRegex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$^#!%*?&()])[A-Za-z\\d@$^#!%*?&()]+$";
        String passRegex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)[A-Za-z\\d]+$";
        Pattern pattern = Pattern.compile(passRegex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }


    public boolean isEmailAlreadyExists(String email) {
        Optional<User> existingUser = userRepository.findByEmail(email);
        return existingUser.isPresent();
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }


//    public User getUser(long id) {
//        Optional<User> user = userRepository.findById(id);
//        return user.orElse(null);
//    }

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
