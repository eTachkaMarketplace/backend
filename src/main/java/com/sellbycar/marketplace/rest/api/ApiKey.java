package com.sellbycar.marketplace.rest.api;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ApiKey {
    public static final String API = "/api";

    // Authorization API
    public static final String AUTH = API + "/auth";
    public static final String AUTH_REGISTER = AUTH + "/register";
    public static final String AUTH_LOGIN = "/login";
    public static final String AUTH_REFRESH_TOKEN = AUTH + "/refresh/token";

    // User API
    public static final String USER = API + "/user";

}
