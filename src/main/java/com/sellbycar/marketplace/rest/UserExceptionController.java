package com.sellbycar.marketplace.rest;

import com.sellbycar.marketplace.rest.exception.CustomUserException;
import com.sellbycar.marketplace.rest.exception.UserEmailException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserExceptionController {

    @ExceptionHandler(CustomUserException.class)
    public ResponseEntity<String> userException(CustomUserException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(UserEmailException.class)
    public ResponseEntity<String> duplicateUserEmail(UserEmailException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
