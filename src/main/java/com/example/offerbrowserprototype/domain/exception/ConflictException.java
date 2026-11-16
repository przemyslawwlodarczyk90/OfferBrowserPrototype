package com.example.offerbrowserprototype.domain.exception;

/** The request clashes with the current state (duplicate entry, removing the last required item) — HTTP 409. */
public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
