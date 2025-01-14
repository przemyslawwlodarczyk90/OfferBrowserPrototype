//package com.example.offerbrowserprototype.domain.exception;
//
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//
// class InvalidRegisterExceptionTest {
//
//
//    @Test
//     void shouldSetErrorMessageCorrectly() {
//
//        String errorMessage = "Invalid registration data provided";
//
//        InvalidRegisterException exception = new InvalidRegisterException(errorMessage);
//
//        assertEquals(errorMessage, exception.getMessage(), "Komunikat błędu powinien być ustawiony poprawnie.");
//    }
//
//    @Test
//     void shouldAllowEmptyErrorMessage() {
//        String errorMessage = "";
//
//        InvalidRegisterException exception = new InvalidRegisterException(errorMessage);
//
//
//        assertEquals(errorMessage, exception.getMessage(), "Komunikat błędu powinien być pusty.");
//    }
//
//
//    @Test
//     void shouldHandleNullErrorMessage() {
//        String errorMessage = null;
//
//        InvalidRegisterException exception = new InvalidRegisterException(errorMessage);
//
//        assertNull(exception.getMessage(), "Komunikat błędu powinien być null.");
//    }
//
//
//    @Test
//     void shouldBeInstanceOfRuntimeException() {
//
//        InvalidRegisterException exception = new InvalidRegisterException("Test message");
//
//
//        assertTrue(exception instanceof RuntimeException, "InvalidRegisterException powinien być instancją RuntimeException.");
//    }
//}
