package com.nanakusa.zanshin.exception;

import org.springframework.http.HttpStatus;

public class GetUserException extends RuntimeException {
    private final HttpStatus status;

    public GetUserException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
