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
 * Testy jednostkowe dla klasy RegisterUserDTO.
 * Klasa przechowuje dane potrzebne do rejestracji użytkownika.
 */
public class RegisterUserDTOTest {

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
     * Test poprawnych danych rejestracyjnych.
     */
    @Test
    public void shouldPassValidationWhenAllFieldsAreValid() {
        // Given: Poprawne dane rejestracyjne
        RegisterUserDTO registerUserDTO = new RegisterUserDTO();
        registerUserDTO.setUsername("ferdynand.kiepski");
        registerUserDTO.setEmail("ferdynand.kiepski@example.com");
        registerUserDTO.setPassword("password123");

        // When: Walidacja danych
        Set<ConstraintViolation<RegisterUserDTO>> violations = validator.validate(registerUserDTO);

        // Then: Walidacja powinna zakończyć się sukcesem
        assertTrue(violations.isEmpty(), "Walidacja powinna zakończyć się sukcesem.");
    }

    /**
     * Test pustego pola `username`.
     */
    @Test
    public void shouldFailValidationWhenUsernameIsBlank() {
        // Given: Dane z pustym username
        RegisterUserDTO registerUserDTO = new RegisterUserDTO();
        registerUserDTO.setUsername("");
        registerUserDTO.setEmail("ferdynand.kiepski@example.com");
        registerUserDTO.setPassword("password123");

        // When: Walidacja danych
        Set<ConstraintViolation<RegisterUserDTO>> violations = validator.validate(registerUserDTO);

        // Then: Powinien zostać zwrócony błąd walidacji dla pola username
        assertFalse(violations.isEmpty(), "Walidacja powinna wykryć pusty username.");
        assertEquals("Username cannot be empty", violations.iterator().next().getMessage());
    }

    /**
     * Test pustego pola `email`.
     */
    @Test
    public void shouldFailValidationWhenEmailIsBlank() {
        // Given: Dane z pustym email
        RegisterUserDTO registerUserDTO = new RegisterUserDTO();
        registerUserDTO.setUsername("ferdynand.kiepski");
        registerUserDTO.setEmail("");
        registerUserDTO.setPassword("password123");

        // When: Walidacja danych
        Set<ConstraintViolation<RegisterUserDTO>> violations = validator.validate(registerUserDTO);

        // Then: Powinien zostać zwrócony błąd walidacji dla pola email
        assertFalse(violations.isEmpty(), "Walidacja powinna wykryć pusty email.");
        assertEquals("Email cannot be empty", violations.iterator().next().getMessage());
    }

    /**
     * Test niepoprawnego pola `email`.
     */
    @Test
    void shouldFailValidationWhenEmailIsInvalid() {
        // Given
        RegisterUserDTO userDTO = new RegisterUserDTO();
        userDTO.setUsername("valid_username");
        userDTO.setEmail("invalid-email"); // Nieprawidłowy e-mail
        userDTO.setPassword("password123");

        // When
        Set<ConstraintViolation<RegisterUserDTO>> violations = validator.validate(userDTO);

        // Then
        boolean hasEmailValidationError = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("email")
                        && v.getMessage().equals("Email should be valid"));

        assertTrue(hasEmailValidationError, "Walidacja powinna wykryć niepoprawny email.");
    }

    /**
     * Test pustego pola `password`.
     */
    @Test
    public void shouldFailValidationWhenPasswordIsBlank() {
        // Given: Dane z pustym password
        RegisterUserDTO registerUserDTO = new RegisterUserDTO();
        registerUserDTO.setUsername("ferdynand.kiepski");
        registerUserDTO.setEmail("ferdynand.kiepski@example.com");
        registerUserDTO.setPassword("");

        // When: Walidacja danych
        Set<ConstraintViolation<RegisterUserDTO>> violations = validator.validate(registerUserDTO);

        // Then: Powinny zostać zwrócone błędy walidacji dla pola password
        assertFalse(violations.isEmpty(), "Walidacja powinna wykryć pusty password.");
        assertTrue(
                violations.stream().anyMatch(v -> v.getMessage().equals("Password cannot be empty")),
                "Powinien zostać zwrócony komunikat: 'Password cannot be empty'"
        );
    }

    /**
     * Test za krótkiego pola `password`.
     */
    @Test
    public void shouldFailValidationWhenPasswordIsTooShort() {
        // Given: Dane z za krótkim password
        RegisterUserDTO registerUserDTO = new RegisterUserDTO();
        registerUserDTO.setUsername("ferdynand.kiepski");
        registerUserDTO.setEmail("ferdynand.kiepski@example.com");
        registerUserDTO.setPassword("123");

        // When: Walidacja danych
        Set<ConstraintViolation<RegisterUserDTO>> violations = validator.validate(registerUserDTO);

        // Then: Powinien zostać zwrócony błąd walidacji dla za krótkiego password
        assertFalse(violations.isEmpty(), "Walidacja powinna wykryć zbyt krótkie password.");
        assertEquals("Password should have at least 6 characters", violations.iterator().next().getMessage());
    }

    /**
     * Test getterów i setterów klasy RegisterUserDTO.
     */
    @Test
    public void shouldSetAndGetValuesCorrectly() {
        // Given: Obiekt RegisterUserDTO z danymi rejestracyjnymi
        RegisterUserDTO registerUserDTO = new RegisterUserDTO();
        registerUserDTO.setUsername("ferdynand.kiepski");
        registerUserDTO.setEmail("ferdynand.kiepski@example.com");
        registerUserDTO.setPassword("password123");

        // When: Odczytujemy wartości z pól obiektu
        String username = registerUserDTO.getUsername();
        String email = registerUserDTO.getEmail();
        String password = registerUserDTO.getPassword();

        // Then: Wartości pól powinny być poprawne
        assertEquals("ferdynand.kiepski", username, "Getter dla username powinien zwrócić poprawną wartość.");
        assertEquals("ferdynand.kiepski@example.com", email, "Getter dla email powinien zwrócić poprawną wartość.");
        assertEquals("password123", password, "Getter dla password powinien zwrócić poprawną wartość.");
    }
}
