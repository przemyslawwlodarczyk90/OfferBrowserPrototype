//package com.example.offerbrowserprototype.domain.dto.loginandregister;
//
//import jakarta.validation.ConstraintViolation;
//import jakarta.validation.Validation;
//import jakarta.validation.Validator;
//import jakarta.validation.ValidatorFactory;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//
//import java.util.Set;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//
// class LoginDtoTest {
//
//    private static Validator validator;
//
//    @BeforeAll
//     static void setUpValidator() {
//        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//        validator = factory.getValidator();
//    }
//
//
//    @Test
//     void shouldPassValidationWhenAllFieldsAreValid() {
//        LoginDto loginDto = new LoginDto();
//        loginDto.setUsername("ferdynand.kiepski");
//        loginDto.setPassword("haslo123");
//
//        Set<ConstraintViolation<LoginDto>> violations = validator.validate(loginDto);
//
//        assertTrue(violations.isEmpty(), "Walidacja powinna zakończyć się sukcesem.");
//    }
//
//
//    @Test
//     void shouldFailValidationWhenUsernameIsBlank() {
//
//        LoginDto loginDto = new LoginDto();
//        loginDto.setUsername("");
//        loginDto.setPassword("haslo123");
//
//        Set<ConstraintViolation<LoginDto>> violations = validator.validate(loginDto);
//
//        assertFalse(violations.isEmpty(), "Walidacja powinna wykryć pusty username.");
//        assertEquals("Username cannot be empty", violations.iterator().next().getMessage());
//    }
//
//    @Test
//     void shouldFailValidationWhenPasswordIsBlank() {
//
//        LoginDto loginDto = new LoginDto();
//        loginDto.setUsername("ferdynand.kiepski");
//        loginDto.setPassword("");
//
//        Set<ConstraintViolation<LoginDto>> violations = validator.validate(loginDto);
//
//        assertFalse(violations.isEmpty(), "Walidacja powinna wykryć puste password.");
//        assertEquals("Password cannot be empty", violations.iterator().next().getMessage());
//    }
//
//    @Test
//     void shouldSetAndGetValuesCorrectly() {
//
//        LoginDto loginDto = new LoginDto();
//        loginDto.setUsername("ferdynand.kiepski");
//        loginDto.setPassword("haslo123");
//
//        String username = loginDto.getUsername();
//        String password = loginDto.getPassword();
//
//        assertEquals("ferdynand.kiepski", username, "Getter dla username powinien zwrócić poprawną wartość.");
//        assertEquals("haslo123", password, "Getter dla password powinien zwrócić poprawną wartość.");
//    }
//}
