package com.ab.auth.controller;

import com.ab.auth.constants.AuthURI;
import com.ab.auth.enums.TwoFAType;
import com.ab.auth.service.TwoFAService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

//TODO 6: Improve your HTTP Methods like POST, GET, DELETE, PUT. Use them properly as per their standards. Return URI in POST if a new resource is created.
@RestController
@RequestMapping(AuthURI.TWO_FA_URI)
@CrossOrigin("*")
public class TwoFAResource {

    private final TwoFAService twoFAService;

    private static final Logger LOGGER = LoggerFactory.getLogger(TwoFAResource.class);

    @Autowired
    public TwoFAResource(TwoFAService Service) {
        twoFAService = Service;
    }

    @PostMapping(value = AuthURI.TWO_FACTOR_VIA_2FA_TYPE_URI, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean insertOTPViaTwoFA(@Valid @NotNull @RequestParam String email, @NotNull @RequestParam TwoFAType twoFAType, HttpServletRequest httpServletRequest) {
        LOGGER.debug("Enter in TwoFAResource.insertOTPViaTwoFA()");
        Boolean response = twoFAService.insertOTPViaTwoFA(email, httpServletRequest, twoFAType);
        LOGGER.debug("Exit in TwoFAResource.insertOTPViaTwoFA()");
        return response;
    }

    @PostMapping(value = AuthURI.TWO_FACTOR_VALIDATE_VIA_2FA_TYPE_URI, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean validateTwoFA(@Valid @NotNull @RequestParam String email, @Valid @NotNull @RequestParam String otp, @NotNull @RequestParam TwoFAType twoFAType, HttpServletRequest httpServletRequest) {
        LOGGER.debug("Enter in TwoFAResource.validateTwoFA()");
        Boolean response = twoFAService.validateOTPTwoFA(email, otp, httpServletRequest, twoFAType);
        LOGGER.debug("Exit in TwoFAResource.validateTwoFA()");
        return response;
    }

}
