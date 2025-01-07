package com.example.offerbrowserprototype.domain.dto.user;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


 class UserDTOTest {

    private Validator validator;


    @BeforeEach
     void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }


    @Test
     void shouldPassValidationWhenAllFieldsAreValid() {

        UserDTO user = new UserDTO("1", "Ferdek", "ferdek@kiepski.pl");

        Set<ConstraintViolation<UserDTO>> violations = validator.validate(user);

        assertTrue(violations.isEmpty(), "Nie powinno być błędów walidacji.");
    }


    @Test
     void shouldFailValidationWhenIdIsBlank() {

        UserDTO user = new UserDTO("", "Ferdek", "ferdek@kiepski.pl");


        Set<ConstraintViolation<UserDTO>> violations = validator.validate(user);

        assertFalse(violations.isEmpty(), "Powinien wystąpić błąd walidacji.");
        assertEquals("ID cannot be empty", violations.iterator().next().getMessage());
    }


    @Test
     void shouldFailValidationWhenUsernameIsBlank() {
        UserDTO user = new UserDTO("1", "", "ferdek@kiepski.pl");

        Set<ConstraintViolation<UserDTO>> violations = validator.validate(user);

        assertFalse(violations.isEmpty(), "Powinien wystąpić błąd walidacji.");
        assertEquals("Username cannot be empty", violations.iterator().next().getMessage());
    }

    @Test
     void shouldFailValidationWhenEmailIsBlank() {

        UserDTO user = new UserDTO("1", "Ferdek", "");

        Set<ConstraintViolation<UserDTO>> violations = validator.validate(user);

        assertFalse(violations.isEmpty(), "Powinien wystąpić błąd walidacji.");
        assertEquals("Email cannot be empty", violations.iterator().next().getMessage());
    }


    @Test
     void shouldFailValidationWhenEmailIsInvalid() {
        UserDTO user = new UserDTO("1", "Ferdek", "kiepski-at-email");

        Set<ConstraintViolation<UserDTO>> violations = validator.validate(user);

        assertFalse(violations.isEmpty(), "Powinien wystąpić błąd walidacji.");
        assertEquals("Email should be valid", violations.iterator().next().getMessage());
    }

    @Test
     void shouldCreateUserDTOUsingConstructors() {

        String id = "1";
        String username = "Ferdek";
        String email = "ferdek@kiepski.pl";

        UserDTO user = new UserDTO(id, username, email);


        assertEquals(id, user.getId());
        assertEquals(username, user.getUsername());
        assertEquals(email, user.getEmail());
    }
}
