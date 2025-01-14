//package com.example.offerbrowserprototype.domain.exception;
//
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
// class UserNotFoundExceptionTest {
//
//
//    @Test
//     void shouldSetErrorMessageCorrectly() {
//
//        String errorMessage = "User not found";
//
//        UserNotFoundException exception = new UserNotFoundException(errorMessage);
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
//        UserNotFoundException exception = new UserNotFoundException(errorMessage);
//
//        assertEquals(errorMessage, exception.getMessage(), "Komunikat błędu powinien być pusty.");
//    }
//
//
//    @Test
//     void shouldHandleNullErrorMessage() {
//        String errorMessage = null;
//
//        UserNotFoundException exception = new UserNotFoundException(errorMessage);
//
//        assertNull(exception.getMessage(), "Komunikat błędu powinien być null.");
//    }
//
//    @Test
//     void shouldBeInstanceOfRuntimeException() {
//
//        UserNotFoundException exception = new UserNotFoundException("Test message");
//
//        assertTrue(exception instanceof RuntimeException, "UserNotFoundException powinien być instancją RuntimeException.");
//    }
//}
