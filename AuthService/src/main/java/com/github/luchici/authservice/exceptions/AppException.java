package com.github.luchici.authservice.exceptions;

import lombok.Getter;

@Getter
public class AppException extends RuntimeException {
    public AppException(final String msg) {
        super(msg);
    }

    public AppException(final Throwable cause) {
        super(cause);
    }
}
