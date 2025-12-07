package com.ab.auth.controller;

import com.ab.auth.annotation.Log;
import com.ab.auth.constants.AuthURI;
import com.ab.auth.dto.LoginUserDTO;
import com.ab.auth.dto.SignUpDTO;
import com.ab.auth.exception.AppException;
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

//TODO 6: Improve your HTTP Methods like POST, GET, DELETE, PUT. Use them properly as per their standards. Return URI in POST if a new resource is created.
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
    @Log
    public ResponseEntity<String> userSignUp(@Valid @NotNull @RequestBody SignUpDTO signUpDTO, HttpServletRequest httpServletRequest) throws AppException {
        String response = userService.userSignUp(signUpDTO.getUser(), signUpDTO.getOtp(), httpServletRequest);
        return ResponseEntity.status(HttpStatus.OK).header(AUTH_HEADER, response).body(USER_SIGNED_UP_SUCCESSFULLY);
    }

    @PostMapping(value = AuthURI.LOGIN_URI, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Log
    public ResponseEntity<Object> userLogin(@Valid @NotNull @RequestBody LoginUserDTO user, HttpServletRequest httpServletRequest) throws AppException {
        JSONObject responseJSON = userService.userLogin(user, httpServletRequest);
        return ResponseEntity.status(HttpStatus.OK).header(AUTH_HEADER, responseJSON.get("token").toString()).body(responseJSON.get("isLoginSuccessful"));
    }

    @GetMapping(value = AuthURI.ME_URI, produces = MediaType.APPLICATION_JSON_VALUE)
    @Log
    public ResponseEntity<Object> userMe() {
        JSONObject responseJSON = userService.userMe();
        return ResponseEntity.status(HttpStatus.OK).body(responseJSON);
    }

    @PostMapping(value = AuthURI.FORGOT_PASSWORD_URI, produces = MediaType.APPLICATION_JSON_VALUE)
    @Log
    public ResponseEntity<Boolean> forgotPassword(@Valid @NotBlank @RequestBody String email, HttpServletRequest httpServletRequest) throws AppException {
        boolean isUserValidatedAndEmailSent = userService.forgotPassword(email, httpServletRequest);
        LOGGER.debug("Email Sent::{}", isUserValidatedAndEmailSent);
        if (isUserValidatedAndEmailSent) {
            return ResponseEntity.status(HttpStatus.OK).body(true);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
        }
    }

    @PostMapping(value = AuthURI.VALIDATE_OTP_URI, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @Log
    public ResponseEntity<?> validateOTP(@Valid @NotBlank @RequestParam String email, @Valid @NotBlank @RequestParam String otp, HttpServletRequest httpServletRequest) throws AppException {
        JSONObject responseJson = userService.validateOTP(email, otp, httpServletRequest);
        boolean isUserAndOTPValidatedAnd = (boolean) responseJson.get("isUserAndOTPValidatedAnd");
        LOGGER.debug("Is UserAndOTPValidatedAnd: {}", isUserAndOTPValidatedAnd);
        if (isUserAndOTPValidatedAnd) {
            return ResponseEntity.status(HttpStatus.OK).header(AUTH_HEADER, responseJson.get("token").toString()).body(true);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
        }
    }

    @PostMapping(value = AuthURI.CHANGE_PASSWORD_URI, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Log
    public ResponseEntity<Boolean> changePassword(@Valid @NotNull @RequestBody LoginUserDTO user, HttpServletRequest httpServletRequest) {
        Boolean result = userService.changePassword(user, httpServletRequest);
        LOGGER.debug("Password Changed::{}", result);
        if (result) {
            return ResponseEntity.status(HttpStatus.OK).body(true);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
        }
    }

    @GetMapping(value = AuthURI.GET_ALL_USERS_URI, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Log
    public ResponseEntity<String[]> getUsers(HttpServletRequest httpServletRequest) {
        String[] result = userService.getUsers();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
