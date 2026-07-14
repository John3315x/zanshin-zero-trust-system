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

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleBadRequest(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(TooManySessionsException.class)
    public ResponseEntity<?> handleTooManySessions(TooManySessionsException ex) {

        Map<String, Object> body = new HashMap<>();
        body.put("error", "SESSION_LIMIT_EXCEEDED");
        body.put("message", ex.getMessage());
        body.put("timestamp", LocalDateTime.now());

        return ResponseEntity.status(ex.getStatus()).body(body);
    }

    @ExceptionHandler(CreateUserException.class)
    public ResponseEntity<?> handleCreateUserEmail(CreateUserException ex) {

        Map<String, Object> body = new HashMap<>();
        body.put("error", "USER_CREATION_FAILED");
        body.put("message", ex.getMessage());
        body.put("timestamp", LocalDateTime.now());

        return ResponseEntity.status(ex.getStatus()).body(body);
    }

    @ExceptionHandler(LoginException.class)
    public ResponseEntity<?> handleLoginError(LoginException ex) {

        Map<String, Object> body = new HashMap<>();
        body.put("error", "LOGIN_FAILED");
        body.put("message", ex.getMessage());
        body.put("timestamp", LocalDateTime.now());

        return ResponseEntity.status(ex.getStatus()).body(body);
    }

    @ExceptionHandler(LogoutException.class)
    public ResponseEntity<?> handleLogoutError(LogoutException ex) {

        Map<String, Object> body = new HashMap<>();
        body.put("error", "LOGOUT_FAILED");
        body.put("message", ex.getMessage());
        body.put("timestamp", LocalDateTime.now());

        return ResponseEntity.status(ex.getStatus()).body(body);
    }

    @ExceptionHandler(RefreshTokenException.class)
    public ResponseEntity<?> handleLogoutError(RefreshTokenException ex) {

        Map<String, Object> body = new HashMap<>();
        body.put("error", "REFRESH_TOKEN_FAILED");
        body.put("message", ex.getMessage());
        body.put("timestamp", LocalDateTime.now());

        return ResponseEntity.status(ex.getStatus()).body(body);
    }
}
