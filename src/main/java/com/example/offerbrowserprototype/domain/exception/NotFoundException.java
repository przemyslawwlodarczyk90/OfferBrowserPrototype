package com.example.offerbrowserprototype.domain.exception;

/** The requested resource does not exist — HTTP 404. */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
