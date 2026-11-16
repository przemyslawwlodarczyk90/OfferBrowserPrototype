package com.example.offerbrowserprototype.domain.exception;

/** Login refused: the registration was never confirmed via the e-mail link. */
public class AccountNotActivatedException extends RuntimeException {
    public AccountNotActivatedException(String message) {
        super(message);
    }
}
