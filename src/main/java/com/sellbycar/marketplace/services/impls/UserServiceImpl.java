package com.sellbycar.marketplace.services.impls;

import com.sellbycar.marketplace.models.dto.UserDTO;
import com.sellbycar.marketplace.models.entities.User;
import com.sellbycar.marketplace.models.enums.UserRole;
import com.sellbycar.marketplace.repositories.UserRepository;
import com.sellbycar.marketplace.services.MailService;
import com.sellbycar.marketplace.services.UserService;
import com.sellbycar.marketplace.utilities.exception.CustomUserException;
import com.sellbycar.marketplace.utilities.exception.UserDataException;
import com.sellbycar.marketplace.utilities.mapper.UserMapper;
import com.sellbycar.marketplace.utilities.payload.request.EmailRequest;
import com.sellbycar.marketplace.utilities.payload.request.LoginRequest;
import com.sellbycar.marketplace.utilities.payload.request.SignupRequest;
import com.sellbycar.marketplace.utilities.validate.Validator;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
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
public class UserServiceImpl implements UserService {
    @Value(("${front.host}"))
    private String host;
    private final UserRepository userRepository;
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final Validator validator;
    private final UserMapper userMapper;

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
//        user.setUniqueCode(UUID.randomUUID().toString());
        userRepository.save(user);
        Context context = new Context();
        context.setVariable("username", user.getFirstName());
        context.setVariable("host", host);
//        context.setVariable("activationCode", user.getUniqueCode());
        mailService.sendSimpleMessage(user.getEmail(), "Registration", "activation_message_uk", context);
        return true;

    }


    public User existByEmail(String email) {
        User user = userRepository.findByEmail(email).orElse(null);

        if (user != null) {
            return user;
        } else {
            throw new BadCredentialsException(String.format("User with email {%s} was not found", user.getEmail()));
        }
    }


    public User findUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }

    public User updateUser(User updatedUser, String emailOfUserForUpdating) {
        Optional<User> findedUser = userRepository.findByEmail(emailOfUserForUpdating);

        if (findedUser.isPresent()) {
            User user = findedUser.get();

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
        Optional<User> foundUser = userRepository.findById(id);
        if (foundUser.isPresent()) {
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
        throw new CustomUserException("User not found");
    }

    @Override
    public UserDTO acceptCode(String uniqueCode) {
        Optional<User> optionalUser = userRepository.findUserByUniqueCode(uniqueCode);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setUniqueCode(null);
            userRepository.save(user);
            return userMapper.toDTO(user);
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

    @Override
    public void activateUser(String uniqueCode) {
        User user = userRepository.findUserByUniqueCode(uniqueCode)
                .orElseThrow(() -> new UserDataException("Bad Request"));

        user.setEnabled(true);
        user.setUniqueCode(null);
        userRepository.save(user);
    }
}
