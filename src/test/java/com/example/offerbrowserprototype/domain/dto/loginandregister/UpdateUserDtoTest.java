package com.example.offerbrowserprototype.domain.dto.loginandregister;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testy jednostkowe dla klasy UpdateUserDto.
 */
public class UpdateUserDtoTest {

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
     * Test walidacji poprawnych danych.
     */
    @Test
    public void shouldPassValidationWhenAllFieldsAreValid() {
        // Given: Poprawne dane
        UpdateUserDto updateUserDto = new UpdateUserDto("1", "arnold.boczek", "arnold@boczek.com");

        // When: Walidujemy obiekt
        Set<ConstraintViolation<UpdateUserDto>> violations = validator.validate(updateUserDto);

        // Then: Nie powinno być żadnych błędów walidacji
        assertTrue(violations.isEmpty(), "Obiekt powinien przejść walidację.");
    }

    /**
     * Test walidacji, gdy ID jest puste.
     */
    @Test
    public void shouldFailValidationWhenIdIsBlank() {
        // Given: Puste ID
        UpdateUserDto updateUserDto = new UpdateUserDto("", "ferdynand.kiepski", "ferdynand@kiepski.com");

        // When: Walidujemy obiekt
        Set<ConstraintViolation<UpdateUserDto>> violations = validator.validate(updateUserDto);

        // Then: Powinien być błąd walidacji dla pola ID
        assertFalse(violations.isEmpty(), "Powinien wystąpić błąd walidacji.");
        assertEquals("ID cannot be empty", violations.iterator().next().getMessage(), "Komunikat błędu powinien być zgodny.");
    }

    /**
     * Test walidacji, gdy username jest pusty.
     */
    @Test
    public void shouldFailValidationWhenUsernameIsBlank() {
        // Given: Puste username
        UpdateUserDto updateUserDto = new UpdateUserDto("1", "", "halina@kiepska.com");

        // When: Walidujemy obiekt
        Set<ConstraintViolation<UpdateUserDto>> violations = validator.validate(updateUserDto);

        // Then: Powinien być błąd walidacji dla pola username
        assertFalse(violations.isEmpty(), "Powinien wystąpić błąd walidacji.");
        assertEquals("Username cannot be empty", violations.iterator().next().getMessage(), "Komunikat błędu powinien być zgodny.");
    }

    /**
     * Test walidacji, gdy email jest pusty.
     */
    @Test
    public void shouldFailValidationWhenEmailIsBlank() {
        // Given: Puste email
        UpdateUserDto updateUserDto = new UpdateUserDto("1", "halina.kiepska", "");

        // When: Walidujemy obiekt
        Set<ConstraintViolation<UpdateUserDto>> violations = validator.validate(updateUserDto);

        // Then: Powinien być błąd walidacji dla pola email
        assertFalse(violations.isEmpty(), "Powinien wystąpić błąd walidacji.");
        assertEquals("Email cannot be empty", violations.iterator().next().getMessage(), "Komunikat błędu powinien być zgodny.");
    }


    /**
     * Test walidacji, gdy email jest nieprawidłowy.
     */
    @Test
    public void shouldFailValidationWhenEmailIsInvalid() {
        // Given: Nieprawidłowy email
        UpdateUserDto updateUserDto = new UpdateUserDto("1", "marian.pazdzioch", "marianpazdzioch"); // Bez znaku @

        // When: Walidujemy obiekt
        Set<ConstraintViolation<UpdateUserDto>> violations = validator.validate(updateUserDto);

        // Then: Powinien być błąd walidacji dla pola email
        assertFalse(violations.isEmpty(), "Powinien wystąpić błąd walidacji.");
        assertEquals("Email should be valid", violations.iterator().next().getMessage(), "Komunikat błędu powinien być zgodny.");
    }
}
