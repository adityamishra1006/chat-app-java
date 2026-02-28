package com.example.auth.utils;

public class AppConstants {
    private AppConstants(){

    }

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String AUTH_HEADER = "Authorization";

    public static final String ROLE_USER = "USER";
    public static final String ROLE_ADMIN = "ADMIN";

    public static final String AUTH_BASE_URL = "/auth";
    public static final String LOGIN_URL = "/auth/login";
    public static final String REGISTER_URL = "/auth/register";
    public static final String REFRESH_URL = "/auth/refresh";
}
