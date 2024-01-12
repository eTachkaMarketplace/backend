package com.sellbycar.marketplace.user;

import com.sellbycar.marketplace.auth.LoginRequest;
import com.sellbycar.marketplace.auth.SignupRequest;
import com.sellbycar.marketplace.mail.MailService;
import com.sellbycar.marketplace.util.exception.RequestException;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final UserRequestValidator userRequestValidator;
    private final UserMapper userMapper;

    public void createNewUser(SignupRequest signUpRequest) throws MessagingException {
        userRequestValidator.throwIfSignupRequestNotValid(signUpRequest);

        String email = signUpRequest.getEmail();
        boolean isEmailAlreadyUsed = userRepository.findByEmail(email).isPresent();
        if (isEmailAlreadyUsed) throw RequestException.conflict("The email is already in use.");

        UserDAO user = new UserDAO();
        user.setEmail(email);
        user.setFirstName(signUpRequest.getName());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.getAuthorities().add(UserAuthority.USER);
        user.setEnabled(true);

        userRepository.save(user);
        mailService.sendRegistrationMail(user);
    }

    public UserDAO findUserByEmailOrThrow(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> RequestException.notFound("User does not exist."));
    }

    public UserDAO updateUser(UserDTO updated, String emailOfUserForUpdating) {
        UserDAO current = userRepository.findByEmail(emailOfUserForUpdating)
                .orElseThrow(() -> RequestException.notFound("User does not exist."));

        Optional.ofNullable(updated.getFirstName()).ifPresent(current::setFirstName);
        Optional.ofNullable(updated.getLastName()).ifPresent(current::setLastName);
        Optional.ofNullable(updated.getPhone()).ifPresent(current::setPhone);

        return userRepository.save(current);
    }

    public boolean deleteUser(long userId) {
        boolean doesUserExist = userRepository.existsById(userId);
        if (!doesUserExist) return false;
        userRepository.deleteById(userId);
        return true;
    }

    public Authentication userAuthentication(UserDAO user) {
        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(user.getEmail());
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @Override
    public void forgotPassword(ForgotPasswordRequest request) {
        UserDAO user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> RequestException.notFound("User not found."));
        user.setUniqueCode(UUID.randomUUID().toString());
        userRepository.save(user);
        try {
            mailService.sendResetPasswordMail(user);
        } catch (MessagingException e) {
            throw RequestException.internal("Could not send email.");
        }
    }

    @Override
    public UserDTO resetUniqueCode(String uniqueCode) {
        UserDAO user = userRepository.findByUniqueCode(uniqueCode)
                .orElseThrow(() -> RequestException.notFound("Invalid code."));
        user.setUniqueCode(null);
        UserDAO updatedUser = userRepository.save(user);
        return userMapper.toDTO(updatedUser);
    }

    @Override
    public void changePassword(LoginRequest request) {
        UserDAO user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> RequestException.notFound("User not found."));
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
    }

    @Override
    public UserDAO getUserFromSecurityContextHolder() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails.getUser();
    }

    @Override
    public void activateUser(String uniqueCode) {
        UserDAO user = userRepository.findByUniqueCode(uniqueCode)
                .orElseThrow(() -> RequestException.notFound("Invalid code."));
        user.setEnabled(true);
        user.setUniqueCode(null);
        userRepository.save(user);
    }
}