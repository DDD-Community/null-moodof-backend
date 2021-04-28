package com.ddd.moodof.adapter.infrastructure.advice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@Getter
@AllArgsConstructor
public class ValidationExceptionResponse{
    private HttpStatus httpStatus;
    private String message;
}
