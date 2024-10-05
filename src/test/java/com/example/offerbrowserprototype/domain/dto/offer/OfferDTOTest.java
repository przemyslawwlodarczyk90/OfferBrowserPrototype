package com.example.offerbrowserprototype.domain.dto.offer;


import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OfferDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenAllFieldsAreValid_thenValidationSucceeds() {
        OfferDTO offerDTO = new OfferDTO(
                "Java Developer",
                "Great job opportunity",
                "New York",
                "60k-80k",
                "Java, Spring Boot",
                false,
                LocalDateTime.now()
        );

        Set<ConstraintViolation<OfferDTO>> violations = validator.validate(offerDTO);

        assertTrue(violations.isEmpty());
    }

    @Test
    void whenTitleIsBlank_thenValidationFails() {
        OfferDTO offerDTO = new OfferDTO(
                "",
                "Great job opportunity",
                "New York",
                "60k-80k",
                "Java, Spring Boot",
                false,
                LocalDateTime.now()
        );

        Set<ConstraintViolation<OfferDTO>> violations = validator.validate(offerDTO);

        assertEquals(1, violations.size());
        assertEquals("Title cannot be empty", violations.iterator().next().getMessage());
    }

    @Test
    void whenDescriptionIsBlank_thenValidationFails() {
        OfferDTO offerDTO = new OfferDTO(
                "Java Developer",
                "",
                "New York",
                "60k-80k",
                "Java, Spring Boot",
                false,
                LocalDateTime.now()
        );

        Set<ConstraintViolation<OfferDTO>> violations = validator.validate(offerDTO);

        assertEquals(1, violations.size());
        assertEquals("Description cannot be empty", violations.iterator().next().getMessage());
    }

    @Test
    void whenLocationIsBlank_thenValidationFails() {
        OfferDTO offerDTO = new OfferDTO(
                "Java Developer",
                "Great job opportunity",
                "",
                "60k-80k",
                "Java, Spring Boot",
                false,
                LocalDateTime.now()
        );

        Set<ConstraintViolation<OfferDTO>> violations = validator.validate(offerDTO);

        assertEquals(1, violations.size());
        assertEquals("Location cannot be empty", violations.iterator().next().getMessage());
    }

    @Test
    void whenAllFieldsAreValidIncludingId_thenValidationSucceeds() {
        OfferDTO offerDTO = new OfferDTO(
                "123",
                "Java Developer",
                "Great job opportunity",
                "New York",
                "60k-80k",
                "Java, Spring Boot",
                false,
                LocalDateTime.now()
        );

        Set<ConstraintViolation<OfferDTO>> violations = validator.validate(offerDTO);

        assertTrue(violations.isEmpty());
    }

    @Test
    void whenOptionalFieldsAreNull_thenValidationSucceeds() {
        OfferDTO offerDTO = new OfferDTO(
                "Java Developer",
                "Great job opportunity",
                "New York",
                null, // salaryRange is null
                null, // technologies is null
                false,
                LocalDateTime.now()
        );

        Set<ConstraintViolation<OfferDTO>> violations = validator.validate(offerDTO);

        assertTrue(violations.isEmpty());
    }
}
