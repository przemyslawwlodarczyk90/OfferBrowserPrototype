package com.example.offerbrowserprototype.domain.user;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    /**
     * Test konstrukcji obiektu `User` za pomocą domyślnego konstruktora.
     */
    @Test
    void shouldCreateUserUsingDefaultConstructor() {
        // When - Tworzenie obiektu
        User user = new User();

        // Then - Weryfikacja domyślnych wartości
        assertNull(user.getId()); // `id` powinno być null domyślnie
        assertNull(user.getUsername()); // `username` powinno być null domyślnie
        assertNull(user.getEmail()); // `email` powinno być null domyślnie
        assertNull(user.getPassword()); // `password` powinno być null domyślnie
        assertFalse(user.isActive()); // `active` powinno być false domyślnie
    }

    /**
     * Test ustawiania i pobierania wartości `id`.
     */
    @Test
    void shouldSetAndGetId() {
        // Given - Dane testowe
        User user = new User();
        String id = "user-id";

        // When - Ustawianie wartości
        user.setId(id);

        // Then - Weryfikacja wartości
        assertEquals(id, user.getId());
    }

    /**
     * Test ustawiania i pobierania wartości `username`.
     */
    @Test
    void shouldSetAndGetUsername() {
        // Given - Dane testowe
        User user = new User();
        String username = "janek_kowalski";

        // When - Ustawianie wartości
        user.setUsername(username);

        // Then - Weryfikacja wartości
        assertEquals(username, user.getUsername());
    }

    /**
     * Test ustawiania i pobierania wartości `email`.
     */
    @Test
    void shouldSetAndGetEmail() {
        // Given - Dane testowe
        User user = new User();
        String email = "janek@example.com";

        // When - Ustawianie wartości
        user.setEmail(email);

        // Then - Weryfikacja wartości
        assertEquals(email, user.getEmail());
    }

    /**
     * Test ustawiania i pobierania wartości `password`.
     */
    @Test
    void shouldSetAndGetPassword() {
        // Given - Dane testowe
        User user = new User();
        String password = "securepassword";

        // When - Ustawianie wartości
        user.setPassword(password);

        // Then - Weryfikacja wartości
        assertEquals(password, user.getPassword());
    }

    /**
     * Test ustawiania i pobierania wartości `active`.
     */
    @Test
    void shouldSetAndGetActive() {
        // Given - Dane testowe
        User user = new User();
        boolean active = true;

        // When - Ustawianie wartości
        user.setActive(active);

        // Then - Weryfikacja wartości
        assertTrue(user.isActive());
    }
}
