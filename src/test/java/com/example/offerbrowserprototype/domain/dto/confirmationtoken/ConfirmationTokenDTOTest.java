//package com.example.offerbrowserprototype.domain.dto.confirmationtoken;
//
//import org.junit.jupiter.api.Test;
//
//import java.time.LocalDateTime;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//
// class ConfirmationTokenDTOTest {
//
//
//    @Test
//     void shouldCreateConfirmationTokenDTOWithAllFields() {
//        String id = "1";
//        String token = "FERDINAND2023";
//        LocalDateTime createdAt = LocalDateTime.of(2023, 12, 1, 12, 0);
//        LocalDateTime expiresAt = LocalDateTime.of(2023, 12, 15, 12, 0);
//        LocalDateTime confirmedAt = LocalDateTime.of(2023, 12, 10, 12, 0);
//        String userId = "FERDINAND_KIEPSKI";
//
//        // Tworzenie obiektu DTO
//        ConfirmationTokenDTO tokenDTO = new ConfirmationTokenDTO(id, token, createdAt, expiresAt, confirmedAt, userId);
//
//        // Walidacja wartości pól
//        assertEquals(id, tokenDTO.getId(), "Pole ID powinno być poprawnie przypisane.");
//        assertEquals(token, tokenDTO.getToken(), "Pole token powinno być poprawnie przypisane.");
//        assertEquals(createdAt, tokenDTO.getCreatedAt(), "Pole createdAt powinno być poprawnie przypisane.");
//        assertEquals(expiresAt, tokenDTO.getExpiresAt(), "Pole expiresAt powinno być poprawnie przypisane.");
//        assertEquals(confirmedAt, tokenDTO.getConfirmedAt(), "Pole confirmedAt powinno być poprawnie przypisane.");
//        assertEquals(userId, tokenDTO.getUserId(), "Pole userId powinno być poprawnie przypisane.");
//    }
//
//
//    @Test
//     void shouldUpdateConfirmedAtField() {
//        // Dane testowe
//        ConfirmationTokenDTO tokenDTO = new ConfirmationTokenDTO();
//        LocalDateTime confirmedAt = LocalDateTime.of(2023, 12, 12, 12, 0);
//
//        // Ustawienie pola confirmedAt
//        tokenDTO.setConfirmedAt(confirmedAt);
//
//        // Walidacja
//        assertEquals(confirmedAt, tokenDTO.getConfirmedAt(), "Pole confirmedAt powinno być poprawnie zaktualizowane.");
//    }
//
//    @Test
//     void shouldThrowExceptionWhenRequiredFieldsAreNull() {
//        ConfirmationTokenDTO tokenDTO = new ConfirmationTokenDTO();
//
//        // Sprawdzanie, czy pola wymagane są ustawione jako null
//        assertNull(tokenDTO.getId(), "Pole ID powinno być początkowo null.");
//        assertNull(tokenDTO.getToken(), "Pole token powinno być początkowo null.");
//        assertNull(tokenDTO.getCreatedAt(), "Pole createdAt powinno być początkowo null.");
//        assertNull(tokenDTO.getExpiresAt(), "Pole expiresAt powinno być początkowo null.");
//        assertNull(tokenDTO.getUserId(), "Pole userId powinno być początkowo null.");
//    }
//}
