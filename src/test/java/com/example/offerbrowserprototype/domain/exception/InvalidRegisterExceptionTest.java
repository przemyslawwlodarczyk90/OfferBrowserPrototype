package com.example.offerbrowserprototype.domain.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testy jednostkowe klasy InvalidRegisterException.
 * Sprawdzają poprawność konstrukcji obiektu oraz przekazywania komunikatu błędu.
 */
public class InvalidRegisterExceptionTest {

    /**
     * Testuje, czy konstruktor poprawnie ustawia komunikat błędu.
     */
    @Test
    public void shouldSetErrorMessageCorrectly() {
        // Given: Komunikat błędu
        String errorMessage = "Invalid registration data provided";

        // When: Tworzymy obiekt InvalidRegisterException z podanym komunikatem
        InvalidRegisterException exception = new InvalidRegisterException(errorMessage);

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

        // When: Tworzymy obiekt InvalidRegisterException z pustym komunikatem
        InvalidRegisterException exception = new InvalidRegisterException(errorMessage);

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

        // When: Tworzymy obiekt InvalidRegisterException z komunikatem null
        InvalidRegisterException exception = new InvalidRegisterException(errorMessage);

        // Then: Komunikat błędu powinien być null
        assertNull(exception.getMessage(), "Komunikat błędu powinien być null.");
    }

    /**
     * Testuje, czy InvalidRegisterException jest instancją RuntimeException.
     */
    @Test
    public void shouldBeInstanceOfRuntimeException() {
        // When: Tworzymy obiekt InvalidRegisterException
        InvalidRegisterException exception = new InvalidRegisterException("Test message");

        // Then: Obiekt powinien być instancją RuntimeException
        assertTrue(exception instanceof RuntimeException, "InvalidRegisterException powinien być instancją RuntimeException.");
    }
}
