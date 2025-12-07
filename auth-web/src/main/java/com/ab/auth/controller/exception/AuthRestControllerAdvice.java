package com.ab.auth.controller.exception;


import com.ab.auth.dto.ExceptionResponse;
import com.ab.auth.exception.AppException;
import com.ab.auth.exception.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthRestControllerAdvice {


    @ExceptionHandler(value = AppException.class)
    public ResponseEntity<String> handleAppException(AppException e) {
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setMessage(e.getMessage());
        exceptionResponse.setCode(e.getErrorCode());
        exceptionResponse.setTraceId(e.getTraceId());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exceptionResponse.toString());
    }


    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setMessage(e.getMessage());
        exceptionResponse.setCode(ErrorCode.INTERNAL_ERROR.name());
        exceptionResponse.setTraceId("generic-exception");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exceptionResponse.toString());
    }
}
