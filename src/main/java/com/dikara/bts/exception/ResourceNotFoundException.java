package com.dikara.bts.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(
            String message
    ) {
        super(message);
    }
}
