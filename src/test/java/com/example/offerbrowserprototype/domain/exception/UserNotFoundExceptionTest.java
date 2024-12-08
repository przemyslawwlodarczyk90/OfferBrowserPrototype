package com.example.offerbrowserprototype.domain.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testy jednostkowe klasy UserNotFoundException.
 * Sprawdzają poprawność konstrukcji obiektu oraz przekazywania komunikatu błędu.
 */
public class UserNotFoundExceptionTest {

    /**
     * Testuje, czy konstruktor poprawnie ustawia komunikat błędu.
     */
    @Test
    public void shouldSetErrorMessageCorrectly() {
        // Given: Komunikat błędu
        String errorMessage = "User not found";

        // When: Tworzymy obiekt UserNotFoundException z podanym komunikatem
        UserNotFoundException exception = new UserNotFoundException(errorMessage);

        // Then: Komunikat błędu powinien być poprawnie przechowywany
        assertEquals(errorMessage, exception.getMessage(), "Komunikat błędu powinien być ustawiony poprawnie.");
    }

    /**
     * Testuje, czy konstruktor poprawnie obsługuje pusty komunikat błędu.
     */
    @Test
    public void shouldAllowEmptyErrorMessage() {
        // Given: Pusty komunikat błędu
        String errorMessage = "";

        // When: Tworzymy obiekt UserNotFoundException z pustym komunikatem
        UserNotFoundException exception = new UserNotFoundException(errorMessage);

        // Then: Komunikat błędu powinien być pusty
        assertEquals(errorMessage, exception.getMessage(), "Komunikat błędu powinien być pusty.");
    }

    /**
     * Testuje, czy konstruktor poprawnie obsługuje wartość null jako komunikat błędu.
     */
    @Test
    public void shouldHandleNullErrorMessage() {
        // Given: Komunikat błędu jako null
        String errorMessage = null;

        // When: Tworzymy obiekt UserNotFoundException z komunikatem null
        UserNotFoundException exception = new UserNotFoundException(errorMessage);

        // Then: Komunikat błędu powinien być null
        assertNull(exception.getMessage(), "Komunikat błędu powinien być null.");
    }

    /**
     * Testuje, czy UserNotFoundException jest instancją RuntimeException.
     */
    @Test
    public void shouldBeInstanceOfRuntimeException() {
        // When: Tworzymy obiekt UserNotFoundException
        UserNotFoundException exception = new UserNotFoundException("Test message");

        // Then: Obiekt powinien być instancją RuntimeException
        assertTrue(exception instanceof RuntimeException, "UserNotFoundException powinien być instancją RuntimeException.");
    }
}
