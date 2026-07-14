package com.nanakusa.zanshin.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CreateUserException.class)
    public ResponseEntity<?> handleCreateUserEmail(CreateUserException ex) {

        Map<String, Object> body = new HashMap<>();
        body.put("error", "USER_CREATION_FAILED");
        body.put("message", ex.getMessage());
        body.put("timestamp", LocalDateTime.now());

        return ResponseEntity.status(ex.getStatus()).body(body);
    }

    @ExceptionHandler(UpdateUserException.class)
    public ResponseEntity<?> handleCreateUserEmail(UpdateUserException ex) {

        Map<String, Object> body = new HashMap<>();
        body.put("error", "USER_UPDATABLE_FAILED");
        body.put("message", ex.getMessage());
        body.put("timestamp", LocalDateTime.now());

        return ResponseEntity.status(ex.getStatus()).body(body);// El status se obtiene del propio exception
    }

    @ExceptionHandler(GetUserException.class)
    public ResponseEntity<?> handleCreateUserEmail(GetUserException ex) {

        Map<String, Object> body = new HashMap<>();
        body.put("error", "GETTING_USER_FAILED");
        body.put("message", ex.getMessage());
        body.put("timestamp", LocalDateTime.now());

        return ResponseEntity.status(ex.getStatus()).body(body);
    }

    @ExceptionHandler(DeleteUserException.class)
    public ResponseEntity<?> handleCreateUserEmail(DeleteUserException ex) {

        Map<String, Object> body = new HashMap<>();
        body.put("error", "USER_DELETION_FAILED");
        body.put("message", ex.getMessage());
        body.put("timestamp", LocalDateTime.now());

        return ResponseEntity.status(ex.getStatus()).body(body);
    }
}
