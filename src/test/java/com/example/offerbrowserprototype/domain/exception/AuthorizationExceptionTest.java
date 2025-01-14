//package com.example.offerbrowserprototype.domain.exception;
//
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//
// class AuthorizationExceptionTest {
//
//
//    @Test
//     void shouldSetErrorMessageCorrectly() {
//
//        String errorMessage = "Unauthorized access attempt detected";
//
//        AuthorizationException exception = new AuthorizationException(errorMessage);
//
//        assertEquals(errorMessage, exception.getMessage(), "Komunikat błędu powinien być ustawiony poprawnie.");
//    }
//
//
//    @Test
//     void shouldAllowEmptyErrorMessage() {
//
//        String errorMessage = "";
//
//        AuthorizationException exception = new AuthorizationException(errorMessage);
//
//
//        assertEquals(errorMessage, exception.getMessage(), "Komunikat błędu powinien być pusty.");
//    }
//
//
//    @Test
//     void shouldHandleNullErrorMessage() {
//
//        String errorMessage = null;
//
//
//        AuthorizationException exception = new AuthorizationException(errorMessage);
//
//        assertNull(exception.getMessage(), "Komunikat błędu powinien być null.");
//    }
//
//
//    @Test
//     void shouldBeInstanceOfRuntimeException() {
//        // When: Tworzymy obiekt AuthorizationException
//        AuthorizationException exception = new AuthorizationException("Test message");
//
//        // Then: Obiekt powinien być instancją RuntimeException
//        assertTrue(exception instanceof RuntimeException, "AuthorizationException powinien być instancją RuntimeException.");
//    }
//}
