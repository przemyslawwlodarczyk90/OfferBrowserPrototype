//package com.example.offerbrowserprototype.domain.dto.loginandregister;
//
//import jakarta.validation.ConstraintViolation;
//import jakarta.validation.Validation;
//import jakarta.validation.Validator;
//import jakarta.validation.ValidatorFactory;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.Set;
//
//import static org.junit.jupiter.api.Assertions.*;
//
// class UpdateUserDtoTest {
//
//    private Validator validator;
//
//    @BeforeEach
//     void setUp() {
//        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//        validator = factory.getValidator();
//    }
//
//    @Test
//     void shouldPassValidationWhenAllFieldsAreValid() {
//        UpdateUserDto updateUserDto = new UpdateUserDto("1", "arnold.boczek", "arnold@boczek.com");
//
//        Set<ConstraintViolation<UpdateUserDto>> violations = validator.validate(updateUserDto);
//
//        assertTrue(violations.isEmpty(), "Obiekt powinien przejść walidację.");
//    }
//
//
//    @Test
//     void shouldFailValidationWhenIdIsBlank() {
//        UpdateUserDto updateUserDto = new UpdateUserDto("", "ferdynand.kiepski", "ferdynand@kiepski.com");
//
//
//        Set<ConstraintViolation<UpdateUserDto>> violations = validator.validate(updateUserDto);
//
//        assertFalse(violations.isEmpty(), "Powinien wystąpić błąd walidacji.");
//        assertEquals("ID cannot be empty", violations.iterator().next().getMessage(), "Komunikat błędu powinien być zgodny.");
//    }
//
//    @Test
//     void shouldFailValidationWhenUsernameIsBlank() {
//        UpdateUserDto updateUserDto = new UpdateUserDto("1", "", "halina@kiepska.com");
//
//        Set<ConstraintViolation<UpdateUserDto>> violations = validator.validate(updateUserDto);
//
//        assertFalse(violations.isEmpty(), "Powinien wystąpić błąd walidacji.");
//        assertEquals("Username cannot be empty", violations.iterator().next().getMessage(), "Komunikat błędu powinien być zgodny.");
//    }
//
//    @Test
//     void shouldFailValidationWhenEmailIsBlank() {
//        UpdateUserDto updateUserDto = new UpdateUserDto("1", "halina.kiepska", "");
//
//        Set<ConstraintViolation<UpdateUserDto>> violations = validator.validate(updateUserDto);
//
//        assertFalse(violations.isEmpty(), "Powinien wystąpić błąd walidacji.");
//        assertEquals("Email cannot be empty", violations.iterator().next().getMessage(), "Komunikat błędu powinien być zgodny.");
//    }
//
//
//    @Test
//     void shouldFailValidationWhenEmailIsInvalid() {
//        UpdateUserDto updateUserDto = new UpdateUserDto("1", "marian.pazdzioch", "marianpazdzioch"); // Bez znaku @
//
//        Set<ConstraintViolation<UpdateUserDto>> violations = validator.validate(updateUserDto);
//
//        assertFalse(violations.isEmpty(), "Powinien wystąpić błąd walidacji.");
//        assertEquals("Email should be valid", violations.iterator().next().getMessage(), "Komunikat błędu powinien być zgodny.");
//    }
//}
