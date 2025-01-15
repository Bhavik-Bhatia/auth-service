package com.ab.auth.exception;

public class InvalidPasswordException extends RuntimeException{
    public InvalidPasswordException(String exceptionMessage){
        super(exceptionMessage);
    }
}
