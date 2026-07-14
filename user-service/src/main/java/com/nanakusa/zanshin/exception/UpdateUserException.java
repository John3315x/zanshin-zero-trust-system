package com.nanakusa.zanshin.exception;

import org.springframework.http.HttpStatus;

public class UpdateUserException extends RuntimeException {

    private final HttpStatus status;

    public UpdateUserException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
