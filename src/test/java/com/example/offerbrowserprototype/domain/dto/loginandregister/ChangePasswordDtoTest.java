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
 * Testy jednostkowe dla klasy ChangePasswordDto.
 * Klasa służy do przechowywania danych wymaganych do zmiany hasła użytkownika.
 */
public class ChangePasswordDtoTest {

    private static Validator validator;

    /**
     * Inicjalizuje walidator dla testów walidacji adnotacji @NotBlank.
     */
    @BeforeAll
    public static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    /**
     * Testuje poprawne utworzenie obiektu z wszystkimi polami wypełnionymi.
     */
    @Test
    public void shouldCreateChangePasswordDtoWithValidData() {
        ChangePasswordDto dto = new ChangePasswordDto();
        dto.setCurrentPassword("ferdynand123");
        dto.setNewPassword("kiepski456");
        dto.setUsername("ferdynand.kiepski");

        // Walidacja poprawności danych
        Set<ConstraintViolation<ChangePasswordDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "Dane w DTO powinny być poprawne.");
    }

    /**
     * Testuje brak wartości w polu currentPassword.
     */
    @Test
    public void shouldDetectBlankCurrentPassword() {
        ChangePasswordDto dto = new ChangePasswordDto();
        dto.setCurrentPassword("");
        dto.setNewPassword("kiepski456");
        dto.setUsername("ferdynand.kiepski");

        Set<ConstraintViolation<ChangePasswordDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty(), "Powinno być naruszenie, gdy currentPassword jest puste.");
        assertEquals("Current password cannot be blank", violations.iterator().next().getMessage());
    }

    /**
     * Testuje brak wartości w polu newPassword.
     */
    @Test
    public void shouldDetectBlankNewPassword() {
        ChangePasswordDto dto = new ChangePasswordDto();
        dto.setCurrentPassword("ferdynand123");
        dto.setNewPassword("");
        dto.setUsername("ferdynand.kiepski");

        Set<ConstraintViolation<ChangePasswordDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty(), "Powinno być naruszenie, gdy newPassword jest puste.");
        assertEquals("New password cannot be blank", violations.iterator().next().getMessage());
    }

    /**
     * Testuje brak wartości w polu username.
     */
    @Test
    public void shouldDetectBlankUsername() {
        ChangePasswordDto dto = new ChangePasswordDto();
        dto.setCurrentPassword("ferdynand123");
        dto.setNewPassword("kiepski456");
        dto.setUsername("");

        Set<ConstraintViolation<ChangePasswordDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty(), "Powinno być naruszenie, gdy username jest puste.");
        assertEquals("Username cannot be blank", violations.iterator().next().getMessage());
    }

    /**
     * Testuje działanie getterów i setterów.
     */
    @Test
    public void shouldSetAndGetValuesCorrectly() {
        ChangePasswordDto dto = new ChangePasswordDto();
        dto.setCurrentPassword("ferdynand123");
        dto.setNewPassword("kiepski456");
        dto.setUsername("ferdynand.kiepski");

        // Walidacja wartości pól
        assertEquals("ferdynand123", dto.getCurrentPassword(), "Getter currentPassword powinien zwracać poprawną wartość.");
        assertEquals("kiepski456", dto.getNewPassword(), "Getter newPassword powinien zwracać poprawną wartość.");
        assertEquals("ferdynand.kiepski", dto.getUsername(), "Getter username powinien zwracać poprawną wartość.");
    }
}
