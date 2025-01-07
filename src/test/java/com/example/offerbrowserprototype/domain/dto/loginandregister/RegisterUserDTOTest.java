package com.example.offerbrowserprototype.domain.dto.loginandregister;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

 class RegisterUserDTOTest {

    private static Validator validator;

    @BeforeAll
     static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
     void shouldPassValidationWhenAllFieldsAreValid() {

        RegisterUserDTO registerUserDTO = new RegisterUserDTO();
        registerUserDTO.setUsername("ferdynand.kiepski");
        registerUserDTO.setEmail("ferdynand.kiepski@example.com");
        registerUserDTO.setPassword("password123");

        Set<ConstraintViolation<RegisterUserDTO>> violations = validator.validate(registerUserDTO);


        assertTrue(violations.isEmpty(), "Walidacja powinna zakończyć się sukcesem.");
    }


    @Test
     void shouldFailValidationWhenUsernameIsBlank() {

        RegisterUserDTO registerUserDTO = new RegisterUserDTO();
        registerUserDTO.setUsername("");
        registerUserDTO.setEmail("ferdynand.kiepski@example.com");
        registerUserDTO.setPassword("password123");

        Set<ConstraintViolation<RegisterUserDTO>> violations = validator.validate(registerUserDTO);


        assertFalse(violations.isEmpty(), "Walidacja powinna wykryć pusty username.");
        assertEquals("Username cannot be empty", violations.iterator().next().getMessage());
    }


    @Test
     void shouldFailValidationWhenEmailIsBlank() {

        RegisterUserDTO registerUserDTO = new RegisterUserDTO();
        registerUserDTO.setUsername("ferdynand.kiepski");
        registerUserDTO.setEmail("");
        registerUserDTO.setPassword("password123");

        Set<ConstraintViolation<RegisterUserDTO>> violations = validator.validate(registerUserDTO);

        assertFalse(violations.isEmpty(), "Walidacja powinna wykryć pusty email.");
        assertEquals("Email cannot be empty", violations.iterator().next().getMessage());
    }


    @Test
    void shouldFailValidationWhenEmailIsInvalid() {

        RegisterUserDTO userDTO = new RegisterUserDTO();
        userDTO.setUsername("valid_username");
        userDTO.setEmail("invalid-email"); // Nieprawidłowy e-mail
        userDTO.setPassword("password123");

        Set<ConstraintViolation<RegisterUserDTO>> violations = validator.validate(userDTO);

        boolean hasEmailValidationError = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("email")
                        && v.getMessage().equals("Email should be valid"));

        assertTrue(hasEmailValidationError, "Walidacja powinna wykryć niepoprawny email.");
    }

    @Test
     void shouldFailValidationWhenPasswordIsBlank() {
        RegisterUserDTO registerUserDTO = new RegisterUserDTO();
        registerUserDTO.setUsername("ferdynand.kiepski");
        registerUserDTO.setEmail("ferdynand.kiepski@example.com");
        registerUserDTO.setPassword("");

        Set<ConstraintViolation<RegisterUserDTO>> violations = validator.validate(registerUserDTO);

        assertFalse(violations.isEmpty(), "Walidacja powinna wykryć pusty password.");
        assertTrue(
                violations.stream().anyMatch(v -> v.getMessage().equals("Password cannot be empty")),
                "Powinien zostać zwrócony komunikat: 'Password cannot be empty'"
        );
    }

    @Test
     void shouldFailValidationWhenPasswordIsTooShort() {
        RegisterUserDTO registerUserDTO = new RegisterUserDTO();
        registerUserDTO.setUsername("ferdynand.kiepski");
        registerUserDTO.setEmail("ferdynand.kiepski@example.com");
        registerUserDTO.setPassword("123");

        Set<ConstraintViolation<RegisterUserDTO>> violations = validator.validate(registerUserDTO);


        assertFalse(violations.isEmpty(), "Walidacja powinna wykryć zbyt krótkie password.");
        assertEquals("Password should have at least 6 characters", violations.iterator().next().getMessage());
    }

    @Test
     void shouldSetAndGetValuesCorrectly() {
        RegisterUserDTO registerUserDTO = new RegisterUserDTO();
        registerUserDTO.setUsername("ferdynand.kiepski");
        registerUserDTO.setEmail("ferdynand.kiepski@example.com");
        registerUserDTO.setPassword("password123");

        String username = registerUserDTO.getUsername();
        String email = registerUserDTO.getEmail();
        String password = registerUserDTO.getPassword();

        assertEquals("ferdynand.kiepski", username, "Getter dla username powinien zwrócić poprawną wartość.");
        assertEquals("ferdynand.kiepski@example.com", email, "Getter dla email powinien zwrócić poprawną wartość.");
        assertEquals("password123", password, "Getter dla password powinien zwrócić poprawną wartość.");
    }
}
