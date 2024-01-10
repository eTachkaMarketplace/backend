package com.sellbycar.marketplace.user;

import com.sellbycar.marketplace.auth.LoginRequest;
import com.sellbycar.marketplace.auth.SignupRequest;
import com.sellbycar.marketplace.mail.MailService;
import com.sellbycar.marketplace.util.exception.CustomUserException;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
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
@Slf4j
public class UserServiceImpl implements UserService {
    @Value(("${front.host}"))
    private String host;
    private final UserRepository userRepository;
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final UserValidator userValidator;
    private final UserMapper userMapper;


    public boolean createNewUser(SignupRequest signUpRequest) throws MessagingException {
        try {
            userValidator.isValidUserInput(signUpRequest);

            String email = signUpRequest.getEmail();

            if (userRepository.findByEmail(email).isPresent()) {
                log.warn("Email is already in use: {}", email);
                return false;
            }

            UserDAO user = new UserDAO();
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

            log.warn("User registered successfully: {}", email);
            return true;
        } catch (Exception e) {
            log.warn("Error during user registration", e);
            throw new MessagingException("Error during user registration", e);
        }
    }


    public UserDAO existByEmail(String email) {
        UserDAO user = userRepository.findByEmail(email).orElse(null);

        if (user != null) {
            return user;
        } else {
            throw new BadCredentialsException(String.format("User with email {%s} was not found", user.getEmail()));
        }
    }


    public UserDAO findUserById(Long id) {
        Optional<UserDAO> user = userRepository.findById(id);
        return user.orElse(null);
    }

    public UserDAO updateUser(UserDAO updatedUser, String emailOfUserForUpdating) {
        Optional<UserDAO> findedUser = userRepository.findByEmail(emailOfUserForUpdating);

        if (findedUser.isPresent()) {
            UserDAO user = findedUser.get();

            Optional.ofNullable(updatedUser.getFirstName()).ifPresent(user::setFirstName);
            Optional.ofNullable(updatedUser.getLastName()).ifPresent(user::setLastName);
            Optional.ofNullable(updatedUser.getPhoto()).ifPresent(user::setPhoto);
            Optional.ofNullable(updatedUser.getPhone()).ifPresent(user::setPhone);

            return userRepository.save(user);
        } else {
            throw new BadCredentialsException("User was not found");
        }
    }

    public boolean deleteUser(long id) {
        Optional<UserDAO> foundUser = userRepository.findById(id);
        if (foundUser.isPresent()) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Authentication userAuthentication(UserDAO user) {
        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(user.getEmail());
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @Override
    public String forgotPassword(ForgotPasswordRequest request) throws MessagingException {
        Optional<UserDAO> optionalUser = userRepository.findByEmail(request.getEmail());
        if (optionalUser.isPresent()) {
            UserDAO user = optionalUser.get();
            user.setUniqueCode(UUID.randomUUID().toString());
            userRepository.save(user);
            Context context = new Context();
            context.setVariable("username", user.getFirstName());
            context.setVariable("host", host);
            context.setVariable("code", user.getUniqueCode());
            mailService.sendSimpleMessage(user.getEmail(), "Registration", "forgot_password_message_uk", context);
            return "Link sent for your email";
        }
        throw new CustomUserException("User not found");
    }

    @Override
    public UserDTO acceptCode(String uniqueCode) {
        Optional<UserDAO> optionalUser = userRepository.findByUniqueCode(uniqueCode);
        if (optionalUser.isPresent()) {
            UserDAO user = optionalUser.get();
            user.setUniqueCode(null);
            userRepository.save(user);
            return userMapper.toDTO(user);
        }
        throw new UserDataException("Bad request");
    }

    @Override
    public String changePassword(LoginRequest request) {
        Optional<UserDAO> optionalUser = userRepository.findByEmail(request.getEmail());
        if (optionalUser.isPresent()) {
            UserDAO user = optionalUser.get();
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            userRepository.save(user);
            return "Success";
        }
        throw new UserDataException("Bad request");
    }

    @Override
    public UserDAO getUserFromSecurityContextHolder() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userRepository.findByEmail(userDetails.getUsername()).get();
    }

    @Override
    public void activateUser(String uniqueCode) {
        UserDAO user = userRepository.findByUniqueCode(uniqueCode)
                .orElseThrow(() -> new UserDataException("Bad Request"));

        user.setEnabled(true);
        user.setUniqueCode(null);
        userRepository.save(user);
    }
}
