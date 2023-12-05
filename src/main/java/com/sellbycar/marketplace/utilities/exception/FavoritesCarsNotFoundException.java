package com.sellbycar.marketplace.utilities.exception;

public class FavoritesCarsNotFoundException extends RuntimeException
{
    public FavoritesCarsNotFoundException()
    {
        super("You did not add a cars to your favorite list");
    }
}
