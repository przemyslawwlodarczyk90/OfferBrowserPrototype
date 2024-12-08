package com.example.offerbrowserprototype.domain.user;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ConfirmationTokenTest {

    /**
     * Test konstrukcji obiektu `ConfirmationToken` przy użyciu konstruktora z parametrami.
     */
    @Test
    void shouldCreateConfirmationTokenUsingParameterizedConstructor() {
        // Given - Dane testowe
        String token = "sample-token";
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime expiresAt = createdAt.plusDays(1);
        String userId = "123";

        // When - Tworzenie obiektu
        ConfirmationToken confirmationToken = new ConfirmationToken(token, createdAt, expiresAt, userId);

        // Then - Weryfikacja stanu obiektu
        assertNull(confirmationToken.getId()); // `id` powinno być null, bo jest ustawiane przez bazę danych
        assertEquals(token, confirmationToken.getToken());
        assertEquals(createdAt, confirmationToken.getCreatedAt());
        assertEquals(expiresAt, confirmationToken.getExpiresAt());
        assertNull(confirmationToken.getConfirmedAt()); // `confirmedAt` powinno być null domyślnie
        assertEquals(userId, confirmationToken.getUserId());
    }

    /**
     * Test ustawiania i pobierania właściwości `confirmedAt`.
     */
    @Test
    void shouldSetAndGetConfirmedAt() {
        // Given - Dane testowe
        ConfirmationToken confirmationToken = new ConfirmationToken();
        LocalDateTime confirmedAt = LocalDateTime.now();

        // When - Ustawianie wartości
        confirmationToken.setConfirmedAt(confirmedAt);

        // Then - Weryfikacja wartości
        assertEquals(confirmedAt, confirmationToken.getConfirmedAt());
    }

    /**
     * Test ustawiania i pobierania właściwości `id`.
     */
    @Test
    void shouldSetAndGetId() {
        // Given - Dane testowe
        ConfirmationToken confirmationToken = new ConfirmationToken();
        String id = "token-id";

        // When - Ustawianie wartości
        confirmationToken.setId(id);

        // Then - Weryfikacja wartości
        assertEquals(id, confirmationToken.getId());
    }

    /**
     * Test ustawiania i pobierania właściwości `token`.
     */
    @Test
    void shouldSetAndGetToken() {
        // Given - Dane testowe
        ConfirmationToken confirmationToken = new ConfirmationToken();
        String token = "sample-token";

        // When - Ustawianie wartości
        confirmationToken.setToken(token);

        // Then - Weryfikacja wartości
        assertEquals(token, confirmationToken.getToken());
    }

    /**
     * Test ustawiania i pobierania właściwości `userId`.
     */
    @Test
    void shouldSetAndGetUserId() {
        // Given - Dane testowe
        ConfirmationToken confirmationToken = new ConfirmationToken();
        String userId = "123";

        // When - Ustawianie wartości
        confirmationToken.setUserId(userId);

        // Then - Weryfikacja wartości
        assertEquals(userId, confirmationToken.getUserId());
    }
}
