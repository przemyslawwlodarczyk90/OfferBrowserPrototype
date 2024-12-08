package com.example.offerbrowserprototype.domain.dto.loginandregister;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testy jednostkowe dla klasy RegistrationResultDTO.
 */
public class RegistrationResultDTOTest {

    /**
     * Test poprawnego działania domyślnego konstruktora.
     */
    @Test
    public void shouldCreateDefaultObject() {
        // Given: Utworzenie obiektu przy użyciu domyślnego konstruktora
        RegistrationResultDTO result = new RegistrationResultDTO();

        // Then: Wszystkie pola powinny być domyślnie zainicjalizowane na null/false
        assertNull(result.getUserId(), "Początkowy userId powinien być null.");
        assertNull(result.getUsername(), "Początkowa nazwa użytkownika powinna być null.");
        assertFalse(result.isSuccess(), "Początkowy wynik powinien być false.");
        assertNull(result.getMessage(), "Początkowa wiadomość powinna być null.");
    }

    /**
     * Test poprawnego działania konstruktora z trzema parametrami.
     */
    @Test
    public void shouldCreateObjectWithThreeParamConstructor() {
        // Given: Dane testowe
        String username = "ferdynand.kiepski";
        boolean isSuccess = false;
        String message = "Rejestracja nie powiodła się";

        // When: Tworzymy obiekt z trzema parametrami
        RegistrationResultDTO result = new RegistrationResultDTO(username, isSuccess, message);

        // Then: Wszystkie pola powinny być poprawnie zainicjalizowane
        assertNull(result.getUserId(), "userId powinno być null dla tego konstruktora.");
        assertEquals(username, result.getUsername(), "Username powinien być zgodny z danymi testowymi.");
        assertEquals(isSuccess, result.isSuccess(), "isSuccess powinno być zgodne z danymi testowymi.");
        assertEquals(message, result.getMessage(), "Wiadomość powinna być zgodna z danymi testowymi.");
    }

    /**
     * Test poprawnego działania konstruktora z wszystkimi parametrami.
     */
    @Test
    public void shouldCreateObjectWithAllParamConstructor() {
        // Given: Dane testowe
        String userId = "1";
        String username = "marian.pazdzioch";
        boolean isSuccess = true;
        String message = "Rejestracja zakończona sukcesem";

        // When: Tworzymy obiekt z wszystkimi parametrami
        RegistrationResultDTO result = new RegistrationResultDTO(userId, username, isSuccess, message);

        // Then: Wszystkie pola powinny być poprawnie zainicjalizowane
        assertEquals(userId, result.getUserId(), "userId powinno być zgodne z danymi testowymi.");
        assertEquals(username, result.getUsername(), "Username powinien być zgodny z danymi testowymi.");
        assertEquals(isSuccess, result.isSuccess(), "isSuccess powinno być zgodne z danymi testowymi.");
        assertEquals(message, result.getMessage(), "Wiadomość powinna być zgodna z danymi testowymi.");
    }

    /**
     * Test poprawnego działania setterów.
     */
    @Test
    public void shouldSetFieldsCorrectly() {
        // Given: Obiekt i dane testowe
        RegistrationResultDTO result = new RegistrationResultDTO();
        String userId = "2";
        String username = "halina.kiepska";
        boolean isSuccess = true;
        String message = "Rejestracja przebiegła pomyślnie";

        // When: Ustawiamy wartości za pomocą setterów
        result.setUserId(userId);
        result.setUsername(username);
        result.setSuccess(isSuccess);
        result.setMessage(message);

        // Then: Wszystkie pola powinny być poprawnie ustawione
        assertEquals(userId, result.getUserId(), "userId powinno być zgodne z danymi testowymi.");
        assertEquals(username, result.getUsername(), "Username powinien być zgodny z danymi testowymi.");
        assertEquals(isSuccess, result.isSuccess(), "isSuccess powinno być zgodne z danymi testowymi.");
        assertEquals(message, result.getMessage(), "Wiadomość powinna być zgodna z danymi testowymi.");
    }
}
