package com.ab.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ExceptionResponse {
    private String code;
    private String message;
    private String traceId;
}
