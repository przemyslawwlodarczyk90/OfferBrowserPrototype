package com.example.offerbrowserprototype.domain.dto.confirmationtoken;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testy jednostkowe dla klasy TokenResponse.
 * Klasa służy do przechowywania tokenu w odpowiedzi na żądanie.
 */
public class TokenResponseTest {

    /**
     * Testuje konstruktor oraz poprawność przypisania wartości do pola token.
     */
    @Test
    public void shouldCreateTokenResponseWithToken() {
        String testToken = "FERDINAND_TOKEN";

        // Tworzenie obiektu TokenResponse
        TokenResponse tokenResponse = new TokenResponse(testToken);

        // Walidacja
        assertEquals(testToken, tokenResponse.getToken(), "Pole token powinno być poprawnie przypisane.");
    }

    /**
     * Testuje setter dla pola token.
     */
    @Test
    public void shouldUpdateTokenField() {
        // Dane testowe
        TokenResponse tokenResponse = new TokenResponse("INITIAL_TOKEN");
        String updatedToken = "KIEPSKI_UPDATED_TOKEN";

        // Ustawienie nowej wartości pola token
        tokenResponse.setToken(updatedToken);

        // Walidacja
        assertEquals(updatedToken, tokenResponse.getToken(), "Pole token powinno być poprawnie zaktualizowane.");
    }

    /**
     * Testuje, czy pole token zwraca wartość null, jeśli nie zostało ustawione.
     */
    @Test
    public void shouldAllowNullToken() {
        // Tworzenie obiektu TokenResponse z null jako wartością tokenu
        TokenResponse tokenResponse = new TokenResponse(null);

        // Walidacja
        assertNull(tokenResponse.getToken(), "Pole token powinno zwracać null, jeśli nie zostało ustawione.");
    }
}
