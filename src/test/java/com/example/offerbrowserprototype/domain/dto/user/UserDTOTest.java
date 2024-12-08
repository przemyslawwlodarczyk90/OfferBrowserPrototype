package com.example.offerbrowserprototype.domain.dto.user;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testy jednostkowe klasy `UserDTO`.
 * Sprawdzają poprawność walidacji oraz poprawne działanie konstruktorów.
 */
public class UserDTOTest {

    private Validator validator;

    /**
     * Inicjalizacja walidatora przed każdym testem.
     */
    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    /**
     * Test walidacji, gdy wszystkie pola są poprawne.
     */
    @Test
    public void shouldPassValidationWhenAllFieldsAreValid() {
        // Given: Poprawne dane użytkownika
        UserDTO user = new UserDTO("1", "Ferdek", "ferdek@kiepski.pl");

        // When: Walidujemy obiekt
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(user);

        // Then: Nie powinno być błędów walidacji
        assertTrue(violations.isEmpty(), "Nie powinno być błędów walidacji.");
    }

    /**
     * Test walidacji, gdy pole `id` jest puste.
     */
    @Test
    public void shouldFailValidationWhenIdIsBlank() {
        // Given: Dane z pustym ID
        UserDTO user = new UserDTO("", "Ferdek", "ferdek@kiepski.pl");

        // When: Walidujemy obiekt
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(user);

        // Then: Powinien być błąd walidacji dla `id`
        assertFalse(violations.isEmpty(), "Powinien wystąpić błąd walidacji.");
        assertEquals("ID cannot be empty", violations.iterator().next().getMessage());
    }

    /**
     * Test walidacji, gdy pole `username` jest puste.
     */
    @Test
    public void shouldFailValidationWhenUsernameIsBlank() {
        // Given: Dane z pustą nazwą użytkownika
        UserDTO user = new UserDTO("1", "", "ferdek@kiepski.pl");

        // When: Walidujemy obiekt
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(user);

        // Then: Powinien być błąd walidacji dla `username`
        assertFalse(violations.isEmpty(), "Powinien wystąpić błąd walidacji.");
        assertEquals("Username cannot be empty", violations.iterator().next().getMessage());
    }

    /**
     * Test walidacji, gdy pole `email` jest puste.
     */
    @Test
    public void shouldFailValidationWhenEmailIsBlank() {
        // Given: Dane z pustym adresem e-mail
        UserDTO user = new UserDTO("1", "Ferdek", "");

        // When: Walidujemy obiekt
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(user);

        // Then: Powinien być błąd walidacji dla `email`
        assertFalse(violations.isEmpty(), "Powinien wystąpić błąd walidacji.");
        assertEquals("Email cannot be empty", violations.iterator().next().getMessage());
    }

    /**
     * Test walidacji, gdy pole `email` ma nieprawidłowy format.
     */
    @Test
    public void shouldFailValidationWhenEmailIsInvalid() {
        // Given: Dane z nieprawidłowym adresem e-mail
        UserDTO user = new UserDTO("1", "Ferdek", "kiepski-at-email");

        // When: Walidujemy obiekt
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(user);

        // Then: Powinien być błąd walidacji dla `email`
        assertFalse(violations.isEmpty(), "Powinien wystąpić błąd walidacji.");
        assertEquals("Email should be valid", violations.iterator().next().getMessage());
    }

    /**
     * Test poprawności działania konstruktorów.
     */
    @Test
    public void shouldCreateUserDTOUsingConstructors() {
        // Given: Dane użytkownika
        String id = "1";
        String username = "Ferdek";
        String email = "ferdek@kiepski.pl";

        // When: Tworzymy obiekt za pomocą konstruktora
        UserDTO user = new UserDTO(id, username, email);

        // Then: Obiekt powinien być poprawnie utworzony
        assertEquals(id, user.getId());
        assertEquals(username, user.getUsername());
        assertEquals(email, user.getEmail());
    }
}
