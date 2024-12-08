package com.example.offerbrowserprototype.domain.dto.loginandregister;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testy jednostkowe dla klasy LoginDto.
 * Klasa przechowuje dane logowania użytkownika.
 */
public class LoginDtoTest {

    private static Validator validator;

    /**
     * Inicjalizacja walidatora przed wykonaniem testów.
     */
    @BeforeAll
    public static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    /**
     * Test poprawnych danych logowania.
     */
    @Test
    public void shouldPassValidationWhenAllFieldsAreValid() {
        // Given: Poprawne dane logowania
        LoginDto loginDto = new LoginDto();
        loginDto.setUsername("ferdynand.kiepski");
        loginDto.setPassword("haslo123");

        // When: Walidacja danych
        Set<ConstraintViolation<LoginDto>> violations = validator.validate(loginDto);

        // Then: Walidacja powinna przejść bez błędów
        assertTrue(violations.isEmpty(), "Walidacja powinna zakończyć się sukcesem.");
    }

    /**
     * Test pustego pola `username`.
     */
    @Test
    public void shouldFailValidationWhenUsernameIsBlank() {
        // Given: Dane logowania z pustym username
        LoginDto loginDto = new LoginDto();
        loginDto.setUsername("");
        loginDto.setPassword("haslo123");

        // When: Walidacja danych
        Set<ConstraintViolation<LoginDto>> violations = validator.validate(loginDto);

        // Then: Powinien zostać zwrócony błąd walidacji dla pola username
        assertFalse(violations.isEmpty(), "Walidacja powinna wykryć pusty username.");
        assertEquals("Username cannot be empty", violations.iterator().next().getMessage());
    }

    /**
     * Test pustego pola `password`.
     */
    @Test
    public void shouldFailValidationWhenPasswordIsBlank() {
        // Given: Dane logowania z pustym password
        LoginDto loginDto = new LoginDto();
        loginDto.setUsername("ferdynand.kiepski");
        loginDto.setPassword("");

        // When: Walidacja danych
        Set<ConstraintViolation<LoginDto>> violations = validator.validate(loginDto);

        // Then: Powinien zostać zwrócony błąd walidacji dla pola password
        assertFalse(violations.isEmpty(), "Walidacja powinna wykryć puste password.");
        assertEquals("Password cannot be empty", violations.iterator().next().getMessage());
    }

    /**
     * Test getterów i setterów klasy LoginDto.
     */
    @Test
    public void shouldSetAndGetValuesCorrectly() {
        // Given: Obiekt LoginDto z danymi logowania
        LoginDto loginDto = new LoginDto();
        loginDto.setUsername("ferdynand.kiepski");
        loginDto.setPassword("haslo123");

        // When: Odczytujemy wartości z pól obiektu
        String username = loginDto.getUsername();
        String password = loginDto.getPassword();

        // Then: Wartości pól powinny być poprawne
        assertEquals("ferdynand.kiepski", username, "Getter dla username powinien zwrócić poprawną wartość.");
        assertEquals("haslo123", password, "Getter dla password powinien zwrócić poprawną wartość.");
    }
}
