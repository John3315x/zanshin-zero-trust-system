package com.nanakusa.zanshin.exception;

import org.springframework.http.HttpStatus;

public class LogoutException extends RuntimeException {
    private final HttpStatus status;

    public LogoutException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
