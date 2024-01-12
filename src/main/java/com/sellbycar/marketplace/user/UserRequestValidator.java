package com.sellbycar.marketplace.user;

import com.sellbycar.marketplace.auth.SignupRequest;
import com.sellbycar.marketplace.util.exception.RequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserRequestValidator { // ToDo write a test for the validations

    public void throwIfSignupRequestNotValid(SignupRequest signUpRequest) {
        String username = signUpRequest.getName();
        String password = signUpRequest.getPassword();
        String email = signUpRequest.getEmail();

        if (username == null || username.length() < 2 || containsDigits(username))
            throw RequestException.bad("Invalid username. Usernames should be at least 2 symbols long and should not contain digits.");
        if (password == null || password.length() < 5 || isNotPasswordValid(password))
            throw RequestException.bad("""
                    The password must meet the following criteria:
                    - At least 5 characters long
                    - Must contain at least one uppercase letter
                    - Must contain at least one lowercase letter
                    - Must contain at least one digit
                    - Must not contain Cyrillic characters""");
        if (email == null || email.isEmpty() || !isEmailValid(email)) {
            throw RequestException.bad("Invalid email address. Email should not be empty and should have a valid format.");
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

    private boolean isEmailValid(String email) {
        String emailRegex = "^[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean isNotPasswordValid(String password) {
        String passRegex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)[A-Za-z\\d!@#$%^&\\-*]{5,}$";
        Pattern pattern = Pattern.compile(passRegex);
        Matcher matcher = pattern.matcher(password);
        return !matcher.matches();
    }
}
