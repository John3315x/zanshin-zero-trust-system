package com.nanakusa.zanshin.exception;

import org.springframework.http.HttpStatus;

public class CreateUserException extends RuntimeException {
    private final HttpStatus status;

    public CreateUserException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
