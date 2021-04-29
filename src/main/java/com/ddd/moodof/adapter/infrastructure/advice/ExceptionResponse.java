package com.ddd.moodof.adapter.infrastructure.advice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;

@NoArgsConstructor
@Getter
@AllArgsConstructor
public class ExceptionResponse {
    private List<String> messages;
}
