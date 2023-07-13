package com.github.luchici.cityservice.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ApiException extends RuntimeException {
    private final LocalDateTime timestamp = LocalDateTime.now();

    public ApiException(HttpStatus status, String message) {
        super(message);
    }

    public ApiException(String message, IOException e) {
        super(message,e);
    }
}
