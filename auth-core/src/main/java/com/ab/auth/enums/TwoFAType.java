package com.ab.auth.enums;

public enum TwoFAType {
    VALIDATE_FORGOT_PASSWORD("ForgotPassword"),
    VALIDATE_SIGNUP("ValidateSignUp"),
    VALIDATE_NEW_DEVICE_LOGIN("NewDeviceLogin");

    TwoFAType(String s) {
    }
}
