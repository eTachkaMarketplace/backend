package com.sellbycar.marketplace.service.impl;

import com.sellbycar.marketplace.persistance.UserRepository;
import com.sellbycar.marketplace.persistance.enums.UserRole;
import com.sellbycar.marketplace.persistance.model.User;
import com.sellbycar.marketplace.rest.exception.UserDataException;
import com.sellbycar.marketplace.rest.payload.request.EmailRequest;
import com.sellbycar.marketplace.rest.payload.request.LoginRequest;
import com.sellbycar.marketplace.rest.payload.request.SignupRequest;
import com.sellbycar.marketplace.service.MailService;
import com.sellbycar.marketplace.service.UserService;
import com.sellbycar.marketplace.service.validate.Validator;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Value(("${front.host}"))
    private String host;
    private final UserRepository userRepository;
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final Validator validator;

    public boolean createNewUser(SignupRequest signUpRequest) throws MessagingException {
        validator.isValidUserInput(signUpRequest);

        String email = signUpRequest.getEmail();

        if (userRepository.findByEmail(email).isPresent()) return false;
        User user = new User();
        user.setEmail(email);
        user.setFirstName(signUpRequest.getName());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.getAuthority().add(UserRole.USER);
        user.setEnabled(true);
        userRepository.save(user);
        Context context = new Context();
        context.setVariable("username", user.getFirstName());
        context.setVariable("host", host);
        mailService.sendSimpleMessage(user.getEmail(), "Registration", "activation_message_uk", context);
        return true;

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

    @Override
    public String forgotPassword(EmailRequest request) throws MessagingException {
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setUniqueCode(UUID.randomUUID().toString());
            userRepository.save(user);
            Context context = new Context();
            context.setVariable("username", user.getFirstName());
            context.setVariable("host", host);
            context.setVariable("code", user.getUniqueCode());
            mailService.sendSimpleMessage(user.getEmail(), "Registration", "forgot_password_message_uk", context);
            return "Link sent for your email";
        }
        return "User not found";
    }

    @Override
    public User acceptCode(String uniqueCode) {
        Optional<User> optionalUser = userRepository.findUserByUniqueCode(uniqueCode);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setUniqueCode(null);
            userRepository.save(user);
            return user;
        }
        throw new UserDataException("Bad request");
    }

    @Override
    public String changePassword(LoginRequest request) {
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            userRepository.save(user);
            return "Success";
        }
        throw new UserDataException("Bad request");
    }

    @Override
    public User getUserFromSecurityContextHolder() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userRepository.findByEmail(userDetails.getUsername()).get();
    }

}
