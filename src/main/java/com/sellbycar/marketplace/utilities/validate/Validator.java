package com.sellbycar.marketplace.utilities.validate;

import com.sellbycar.marketplace.repositories.UserRepository;
import com.sellbycar.marketplace.models.entities.User;
import com.sellbycar.marketplace.utilities.exception.UserDataException;
import com.sellbycar.marketplace.utilities.payload.request.SignupRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class Validator {

    private final UserRepository userRepository;

    public void isValidUserInput(SignupRequest signUpRequest) {
        String username = signUpRequest.getName();
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

    public boolean isPasswordValid(String password) {
        String passRegex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)[A-Za-z\\d]+$";
        Pattern pattern = Pattern.compile(passRegex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }


    public boolean isEmailAlreadyExists(String email) {
        Optional<User> existingUser = userRepository.findByEmail(email);
        return existingUser.isPresent();
    }
}
