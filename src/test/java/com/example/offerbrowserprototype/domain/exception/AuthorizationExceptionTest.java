package com.example.offerbrowserprototype.domain.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testy jednostkowe klasy AuthorizationException.
 * Sprawdzają poprawność konstrukcji obiektu oraz przekazywania komunikatu błędu.
 */
public class AuthorizationExceptionTest {

    /**
     * Testuje, czy konstruktor poprawnie ustawia komunikat błędu.
     */
    @Test
    public void shouldSetErrorMessageCorrectly() {
        // Given: Komunikat błędu
        String errorMessage = "Unauthorized access attempt detected";

        // When: Tworzymy obiekt AuthorizationException z podanym komunikatem
        AuthorizationException exception = new AuthorizationException(errorMessage);

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

        // When: Tworzymy obiekt AuthorizationException z pustym komunikatem
        AuthorizationException exception = new AuthorizationException(errorMessage);

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

        // When: Tworzymy obiekt AuthorizationException z komunikatem null
        AuthorizationException exception = new AuthorizationException(errorMessage);

        // Then: Komunikat błędu powinien być null
        assertNull(exception.getMessage(), "Komunikat błędu powinien być null.");
    }

    /**
     * Testuje, czy AuthorizationException jest instancją RuntimeException.
     */
    @Test
    public void shouldBeInstanceOfRuntimeException() {
        // When: Tworzymy obiekt AuthorizationException
        AuthorizationException exception = new AuthorizationException("Test message");

        // Then: Obiekt powinien być instancją RuntimeException
        assertTrue(exception instanceof RuntimeException, "AuthorizationException powinien być instancją RuntimeException.");
    }
}
