package com.ab.auth.service;

import com.ab.auth.client.EmailClient;
import com.ab.auth.enums.TwoFAType;
import com.ab.auth.helper.GlobalHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Service
public class TwoFAService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TwoFAService.class);

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private final GlobalHelper globalHelper;

    private final EmailClient emailClient;

    @Autowired
    public TwoFAService(GlobalHelper helper, EmailClient client) {
        globalHelper = helper;
        emailClient = client;
    }

    public Boolean insertOTPViaTwoFA(String email, HttpServletRequest httpServletRequest, TwoFAType twoFAType) {
        LOGGER.debug("Enter in TwoFAService.insertOTPViaTwoFA()");

        LOGGER.debug("Performing Validation");
        Set<ConstraintViolation<String>> violations = validator.validate(email);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<String> constraintViolation : violations) {
                sb.append(constraintViolation.getMessage());
            }
            throw new ConstraintViolationException("Error occurred: " + sb, violations);
        }

//      Generate OTP
        String otp;
        try {
            otp = globalHelper.generateOTP();
        } catch (Exception e) {
            LOGGER.error("Error while generating OTP, message: {}", e.getMessage());
            return false;
        }

        if (!otp.isBlank()) {
            LOGGER.debug("OTP generated");
//          Send mail to user for validating
            Map<String, String> prepareSendMailMap = globalHelper.prepareSendMailMap(email, twoFAType);
            prepareSendMailMap.put("text", String.format("Hi please use this OTP %s to sign up", otp));
            emailClient.sendEmail(prepareSendMailMap);
        }
//      Store OTP in cache
        Boolean result = globalHelper.insertTwoFAOTP(otp, email, twoFAType);
        LOGGER.debug("Exit in TwoFAService.insertOTPViaTwoFA()");
        return result;
    }

    /**
     * Validates inputted OTP by user via cache stored OTP
     *
     * @param email
     * @param otp
     * @param httpServletRequest
     * @param twoFAType
     * @return
     */
    public Boolean validateOTPTwoFA(String email, String otp, HttpServletRequest httpServletRequest, TwoFAType twoFAType) {
        String twoFactorAuthOTP = globalHelper.getTwoFactorAuthOTP(email, twoFAType);
        if (twoFactorAuthOTP.isBlank() || (!twoFactorAuthOTP.equals(otp))) {
            LOGGER.debug("2 factor authentication failed, OTP not valid for {}", email);
            return false;
        }
        return true;
    }

}
