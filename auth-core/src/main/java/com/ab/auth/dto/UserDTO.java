package com.ab.auth.dto;

import com.ab.auth.constants.AuthConstants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long userId;

    @NotBlank(message = AuthConstants.USERNAME_REQUIRED_MESSAGE)
    private String userName;

    @Email(message = AuthConstants.INVALID_EMAIL_MESSAGE)
    @NotBlank(message = AuthConstants.EMAIL_REQUIRED_MESSAGE)
    private String email;

    @NotBlank(message = AuthConstants.MOBILE_NUMBER_REQUIRED_MESSAGE)
    private String mobileNumber;

    private int authProvider;

    @NotBlank(message = AuthConstants.PASSWORD_REQUIRED_MESSAGE)
    @Size(min = 8, max = 20, message = AuthConstants.PASSWORD_VALIDATION_MESSAGE)
    private String hashedPassword;

    @JsonIgnore
    private boolean isDeleted;
}
