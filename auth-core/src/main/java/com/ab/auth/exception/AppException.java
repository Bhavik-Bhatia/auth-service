package com.ab.auth.exception;

import lombok.Getter;

import java.util.UUID;

@Getter
public class AppException extends Exception {

    private final String traceId;
    private final String errorCode;

    public AppException(ErrorCode errorCode, String message) {
        super(message);
        this.traceId = UUID.randomUUID().toString();
        this.errorCode = errorCode.name();
    }
}
