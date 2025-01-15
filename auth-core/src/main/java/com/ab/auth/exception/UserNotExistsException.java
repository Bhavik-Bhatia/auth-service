package com.ab.auth.exception;


public class UserNotExistsException extends RuntimeException{
    public UserNotExistsException(String exceptionMessage){
        super(exceptionMessage);
    }
}
