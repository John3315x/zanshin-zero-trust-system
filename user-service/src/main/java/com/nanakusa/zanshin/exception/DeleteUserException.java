package com.nanakusa.zanshin.exception;

import org.springframework.http.HttpStatus;

public class DeleteUserException extends RuntimeException {
    private final HttpStatus status;

    public DeleteUserException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
