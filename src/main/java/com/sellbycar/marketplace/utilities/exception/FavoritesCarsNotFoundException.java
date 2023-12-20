package com.sellbycar.marketplace.utilities.exception;

public class FavoritesCarsNotFoundException extends RuntimeException
{
    public FavoritesCarsNotFoundException(String message)
    {
        super(message);
    }
}
