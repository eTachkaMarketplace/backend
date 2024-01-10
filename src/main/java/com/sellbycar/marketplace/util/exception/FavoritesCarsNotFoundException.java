package com.sellbycar.marketplace.util.exception;

public class FavoritesCarsNotFoundException extends RuntimeException {
    public FavoritesCarsNotFoundException(String message) {
        super(message);
    }
}
