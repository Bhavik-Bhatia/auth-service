package com.ab.auth.helper;

import com.ab.cache_service.service.CacheService;
import com.ab.auth.enums.TwoFAType;
import com.ab.auth.constants.AuthConstants;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import static com.ab.auth.constants.AuthConstants.*;

@Component
public class GlobalHelper {

    private final CacheService cacheService;

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalHelper.class);

    public GlobalHelper(CacheService service) {
        cacheService = service;
    }

    /**
     * Generates random OTP of length 6 for forgot password
     *
     * @return String
     */
    public String generateOTP() throws NoSuchAlgorithmException {
        SecureRandom secureRandom = SecureRandom.getInstanceStrong();
        int sixDigitNumber = 100000 + secureRandom.nextInt(900000);
        return String.valueOf(sixDigitNumber);
    }

    /**
     * Insert OTP for 2-factor auth
     *
     * @param otp   cache value
     * @param email cache key
     * @return Boolean
     */
    public Boolean insertTwoFAOTP(String otp, String email, TwoFAType twoFAType) {
        try {
            Map<String, Object> map = new HashMap<>();
            switch (twoFAType) {
                case VALIDATE_SIGNUP -> map.put(email + AuthConstants.TWO_FACTOR_OTP_CACHE, otp);
                case VALIDATE_FORGOT_PASSWORD -> map.put(email + AuthConstants.CACHE_FORGOT_PASSWORD, otp);
                case VALIDATE_NEW_DEVICE_LOGIN -> map.put(email + AuthConstants.TWO_FACTOR_OTP_CACHE, otp);
            }
            cacheService.cacheOps(map, CacheService.CacheOperation.INSERT);
        } catch (Exception e) {
            LOGGER.error("Error while inserting 2 factor cache details: {}", e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Get OTP for 2-factor auth
     *
     * @param email cache key
     * @return Boolean
     */
    public String getTwoFactorAuthOTP(String email, TwoFAType twoFAType) {
        String otp = null;
        try {
            Map<String, Object> map = new HashMap<>();
            switch (twoFAType) {
                case VALIDATE_SIGNUP -> map.put(email + TWO_FACTOR_OTP_CACHE, null);
                case VALIDATE_FORGOT_PASSWORD -> map.put(email + CACHE_FORGOT_PASSWORD, null);
                case VALIDATE_NEW_DEVICE_LOGIN -> map.put(email + TWO_FACTOR_OTP_CACHE, null);
            }
            otp = (String) cacheService.cacheOps(map, CacheService.CacheOperation.FETCH).getFirst();
        } catch (Exception e) {
            LOGGER.error("Error while getting 2 factor cache details: {}", e.getMessage());
        }
        return otp;
    }


    /**
     * Map which contains, mail sending details
     *
     * @return Map
     */
    public Map<String, String> prepareSendMailMap(String mailTo, TwoFAType twoFAType) {
        Map<String, String> map = new HashMap<>();
        map.put("isMultipart", "false");
        map.put("mailTo", mailTo);
        switch (twoFAType) {
            case TwoFAType.VALIDATE_FORGOT_PASSWORD:
                map.put("subject", FORGOT_PASSWORD_SUBJECT);
                break;
            case TwoFAType.VALIDATE_SIGNUP:
                map.put("subject", VALIDATE_SIGN_UP_MAIL_SUBJECT);
                break;
            case TwoFAType.VALIDATE_NEW_DEVICE_LOGIN:
                map.put("subject", NEW_DEVICE_LOGIN_MAIL_SUBJECT);
                break;
        }
        return map;
    }

    /**
     * We check if DeviceId is present if not we throw exception. This method is useful for
     * excluded URIs
     *
     * @param httpServletRequest We get deviceHeader from request object
     */
    public String validateDeviceHeader(HttpServletRequest httpServletRequest) {
        String deviceId = httpServletRequest.getHeader("deviceId");
        if (deviceId == null)
            throw new RuntimeException("Error occurred: DeviceId not found");
        return deviceId;
    }

}
