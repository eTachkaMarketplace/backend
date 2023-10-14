package com.sellbycar.marketplace.rest.exception;

public class CustomUserException extends RuntimeException {
    public CustomUserException(String message) {
        super(message);
    }
}
