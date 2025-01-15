package com.ab.auth.controller;

import com.ab.auth.constants.AuthURI;
import com.ab.auth.dto.SignUpDTO;
import com.ab.auth.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static com.ab.auth.constants.AuthConstants.AUTH_HEADER;
import static com.ab.auth.constants.AuthConstants.USER_SIGNED_UP_SUCCESSFULLY;

@RestController
@RequestMapping(AuthURI.USER_URI)
@CrossOrigin("*")
public class UserResources {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserResources.class);

    private UserService userService;

    @Autowired
    public UserResources(UserService service) {
        userService = service;
    }


    @PostMapping(value = AuthURI.SIGN_UP_URI, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> userSignUp(@Valid @NotNull @RequestBody SignUpDTO signUpDTO, HttpServletRequest httpServletRequest) {
        LOGGER.debug("Enter in UserResources.userSignUp()");
        String response = userService.userSignUp(signUpDTO.getUser(), signUpDTO.getOtp(), httpServletRequest);
        LOGGER.debug("Exit in UserResources.userSignUp()");
        return ResponseEntity.status(HttpStatus.OK).header(AUTH_HEADER, response).body(USER_SIGNED_UP_SUCCESSFULLY);
    }

    @PostMapping(value = AuthURI.LOGIN_URI, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> userLogin(@Valid @NotNull @RequestBody LoginUserDTO user, HttpServletRequest httpServletRequest) {
        LOGGER.debug("Enter in UserResources.userLogin()");
        JSONObject responseJSON = userService.userLogin(user, httpServletRequest);
        LOGGER.debug("Exit in UserResources.userLogin()");
        return ResponseEntity.status(HttpStatus.OK).header(AUTH_HEADER, responseJSON.get("token").toString()).body(responseJSON.get("isLoginSuccessful"));
    }

    @GetMapping(value = AuthURI.ME_URI, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> userMe() {
        LOGGER.debug("Enter in UserResources.userMe()");
        JSONObject responseJSON = userService.userMe();
        LOGGER.debug("Exit in UserResources.userMe()");
        return ResponseEntity.status(HttpStatus.OK).body(responseJSON);
    }

    @PostMapping(value = AuthURI.FORGOT_PASSWORD_URI, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> forgotPassword(@Valid @NotBlank @RequestBody String email, HttpServletRequest httpServletRequest) {
        LOGGER.debug("Enter in UserResources.forgotPassword()");
        boolean isUserValidatedAndEmailSent = userService.forgotPassword(email, httpServletRequest);
        if (isUserValidatedAndEmailSent) {
            LOGGER.debug("Exit in UserResources.forgotPassword() Success");
            return ResponseEntity.status(HttpStatus.OK).body(true);
        } else {
            LOGGER.debug("Exit in UserResources.forgotPassword() Failure");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
        }
    }

    @PostMapping(value = AuthURI.VALIDATE_OTP_URI, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<?> validateOTP(@Valid @NotBlank @RequestParam String email, @Valid @NotBlank @RequestParam String otp, HttpServletRequest httpServletRequest) {
        LOGGER.debug("Enter in UserResources.validateOTP()");
        JSONObject responseJson = userService.validateOTP(email, otp, httpServletRequest);
        boolean isUserAndOTPValidatedAnd = (boolean) responseJson.get("isUserAndOTPValidatedAnd");
        if (isUserAndOTPValidatedAnd) {
            LOGGER.debug("Exit in UserResources.validateOTP() Success");
            return ResponseEntity.status(HttpStatus.OK).header(AUTH_HEADER, responseJson.get("token").toString()).body(true);
        } else {
            LOGGER.debug("Exit in UserResources.validateOTP() Failure");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
        }
    }

    @PostMapping(value = AuthURI.CHANGE_PASSWORD_URI, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> changePassword(@Valid @NotNull @RequestBody LoginUserDTO user, HttpServletRequest httpServletRequest) {
        LOGGER.debug("Enter in UserResources.changePassword()");
        Boolean result = userService.changePassword(user, httpServletRequest);
        if (result) {
            LOGGER.debug("Exit in UserResources.changePassword() Success");
            return ResponseEntity.status(HttpStatus.OK).body(true);
        } else {
            LOGGER.debug("Exit in UserResources.changePassword() Failure");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
        }
    }


}
