package com.dikara.bts.common;

import com.dikara.bts.exception.DuplicateResourceException;
import com.dikara.bts.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiResponse<Object>> handleDuplicateResource(DuplicateResourceException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(
                        ApiResponse.builder()
                                .statusCode(HttpStatus.CONFLICT.value())
                                .message(ex.getMessage())
                                .data(Collections.emptyMap())
                                .build()
                );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(
                        ApiResponse.builder()
                                .statusCode(HttpStatus.NOT_FOUND.value())
                                .message(ex.getMessage())
                                .data(Collections.emptyMap())
                                .build()
                );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Object>> handleBadCredentials(
            BadCredentialsException ex
    ) {

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(
                        ApiResponse.builder()
                                .statusCode(HttpStatus.UNAUTHORIZED.value())
                                .message("Invalid email or password")
                                .data(Collections.emptyMap())
                                .build()
                );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidation(
            MethodArgumentNotValidException ex
    ) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        ));

        return ResponseEntity.badRequest().body(
                ApiResponse.<Map<String, String>>builder()
                        .statusCode(400)
                        .message("Validation Failed")
                        .data(errors)
                        .build()
        );
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAuthorization(
            AuthorizationDeniedException ex
    ) {

        return ResponseEntity.status(403)
                .body(
                        ApiResponse.<Void>builder()
                                .statusCode(403)
                                .message("Access Denied")
                                .build()
                );
    }
}
