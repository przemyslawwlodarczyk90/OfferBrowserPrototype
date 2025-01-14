//package com.example.offerbrowserprototype.domain.user;
//
//import org.junit.jupiter.api.Test;
//
//import java.time.LocalDateTime;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class ConfirmationTokenTest {
//
//
//    @Test
//    void shouldCreateConfirmationTokenUsingParameterizedConstructor() {
//        // Given
//        String token = "sample-token";
//        LocalDateTime createdAt = LocalDateTime.now();
//        LocalDateTime expiresAt = createdAt.plusDays(1);
//        String userId = "123";
//
//        // When
//        ConfirmationToken confirmationToken = new ConfirmationToken(token, createdAt, expiresAt, userId);
//
//        // Then
//        assertNull(confirmationToken.getId());
//        assertEquals(token, confirmationToken.getToken());
//        assertEquals(createdAt, confirmationToken.getCreatedAt());
//        assertEquals(expiresAt, confirmationToken.getExpiresAt());
//        assertNull(confirmationToken.getConfirmedAt());
//        assertEquals(userId, confirmationToken.getUserId());
//    }
//
//
//    @Test
//    void shouldSetAndGetConfirmedAt() {
//        // Given
//        ConfirmationToken confirmationToken = new ConfirmationToken();
//        LocalDateTime confirmedAt = LocalDateTime.now();
//
//        // When
//        confirmationToken.setConfirmedAt(confirmedAt);
//
//        // Then
//        assertEquals(confirmedAt, confirmationToken.getConfirmedAt());
//    }
//
//
//    @Test
//    void shouldSetAndGetId() {
//        // Given
//        ConfirmationToken confirmationToken = new ConfirmationToken();
//        String id = "token-id";
//
//        // When
//        confirmationToken.setId(id);
//
//        // Then
//        assertEquals(id, confirmationToken.getId());
//    }
//
//
//    @Test
//    void shouldSetAndGetToken() {
//        // Given
//        ConfirmationToken confirmationToken = new ConfirmationToken();
//        String token = "sample-token";
//
//        // When
//        confirmationToken.setToken(token);
//
//        // Then
//        assertEquals(token, confirmationToken.getToken());
//    }
//
//
//    @Test
//    void shouldSetAndGetUserId() {
//        // Given
//        ConfirmationToken confirmationToken = new ConfirmationToken();
//        String userId = "123";
//
//        // When
//        confirmationToken.setUserId(userId);
//
//        // Then
//        assertEquals(userId, confirmationToken.getUserId());
//    }
//}
