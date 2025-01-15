package com.ab.auth.constants;

public interface AuthConstants {
    String TWO_FACTOR_OTP_CACHE = "#tfoc";
    String CACHE_FORGOT_PASSWORD = "#fp";
    String NEW_DEVICE_LOGIN_MAIL_SUBJECT = "Your account was logged in with a new device!";
    String VALIDATE_SIGN_UP_MAIL_SUBJECT = "Validate your sign up with this OTP";
    String FORGOT_PASSWORD_SUBJECT = "Validate your sign in with this OTP";
    String INVALID_EMAIL_MESSAGE = "Invalid email format!";
    String EMAIL_REQUIRED_MESSAGE = "email is required!";
    String PASSWORD_REQUIRED_MESSAGE = "Password is required!";
    String DEVICE_ID_REQUIRED_MESSAGE = "Device identifier is required!";
    String PASSWORD_VALIDATION_MESSAGE = "Password must be between 8 and 20 characters long";
    String USERNAME_REQUIRED_MESSAGE = "Username is required!";
    String MOBILE_NUMBER_REQUIRED_MESSAGE = "Mobile Number is required!";
    String USER_ALREADY_EXISTS_MESSAGE = "User already exists!";
    String USER_DOES_NOT_EXIST_MESSAGE = "User does not exist!";
    String INVALID_PASSWORD_MESSAGE = "Password is invalid!";
    String USER_SIGNED_UP_SUCCESSFULLY = "User Registered Successfully";
    String TOKEN_INVALID = "Invalid authorization header!";
    String TOKEN_EXPIRED = "User token expired!";
    String AUTHENTICATION_REQUIRED = "User needs to be authenticated!";
    String UNIDENTIFIED_DEVICE = "User device is not recognizable!";
    String AUTH_HEADER = "Authorization";
    String CACHE_USER_DETAILS = "#usd";
    String CACHE_DEVICE_DETAILS = "#udd";
}
