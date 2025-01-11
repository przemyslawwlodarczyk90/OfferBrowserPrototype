package com.example.offerbrowserprototype.domain.exception;

public class OfferAlreadyAppliedException extends RuntimeException {
    public OfferAlreadyAppliedException(String message) {
        super(message);
    }
}
