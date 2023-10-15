package com.sellbycar.marketplace.rest.api;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ApiKey {
    public static final String API = "/api";

    // Authorization API
    public static final String AUTH = API + "/auth";
    public static final String AUTH_REGISTER = AUTH + "/register";
    public static final String AUTH_LOGIN = AUTH + "/login";
    public static final String AUTH_LOGOUT = AUTH + "/logout";
    public static final String AUTH_REFRESH_TOKEN = AUTH + "/refresh/token";
    public static final String AUTH_OAUTH2_SUCCESS = AUTH + "/oauth2/success";

    // User API
    public static final String USER = API + "/user";

}
