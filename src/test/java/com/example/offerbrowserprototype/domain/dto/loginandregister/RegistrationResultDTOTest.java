package com.example.offerbrowserprototype.domain.dto.loginandregister;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


 class RegistrationResultDTOTest {

    @Test
     void shouldCreateDefaultObject() {

        RegistrationResultDTO result = new RegistrationResultDTO();

        assertNull(result.getUserId(), "Początkowy userId powinien być null.");
        assertNull(result.getUsername(), "Początkowa nazwa użytkownika powinna być null.");
        assertFalse(result.isSuccess(), "Początkowy wynik powinien być false.");
        assertNull(result.getMessage(), "Początkowa wiadomość powinna być null.");
    }


    @Test
     void shouldCreateObjectWithThreeParamConstructor() {

        String username = "ferdynand.kiepski";
        boolean isSuccess = false;
        String message = "Rejestracja nie powiodła się";

        RegistrationResultDTO result = new RegistrationResultDTO(username, isSuccess, message);

        assertNull(result.getUserId(), "userId powinno być null dla tego konstruktora.");
        assertEquals(username, result.getUsername(), "Username powinien być zgodny z danymi testowymi.");
        assertEquals(isSuccess, result.isSuccess(), "isSuccess powinno być zgodne z danymi testowymi.");
        assertEquals(message, result.getMessage(), "Wiadomość powinna być zgodna z danymi testowymi.");
    }


    @Test
     void shouldCreateObjectWithAllParamConstructor() {

        String userId = "1";
        String username = "marian.pazdzioch";
        boolean isSuccess = true;
        String message = "Rejestracja zakończona sukcesem";

        RegistrationResultDTO result = new RegistrationResultDTO(userId, username, isSuccess, message);

        assertEquals(userId, result.getUserId(), "userId powinno być zgodne z danymi testowymi.");
        assertEquals(username, result.getUsername(), "Username powinien być zgodny z danymi testowymi.");
        assertEquals(isSuccess, result.isSuccess(), "isSuccess powinno być zgodne z danymi testowymi.");
        assertEquals(message, result.getMessage(), "Wiadomość powinna być zgodna z danymi testowymi.");
    }


    @Test
     void shouldSetFieldsCorrectly() {
        RegistrationResultDTO result = new RegistrationResultDTO();
        String userId = "2";
        String username = "halina.kiepska";
        boolean isSuccess = true;
        String message = "Rejestracja przebiegła pomyślnie";

        result.setUserId(userId);
        result.setUsername(username);
        result.setSuccess(isSuccess);
        result.setMessage(message);

        assertEquals(userId, result.getUserId(), "userId powinno być zgodne z danymi testowymi.");
        assertEquals(username, result.getUsername(), "Username powinien być zgodny z danymi testowymi.");
        assertEquals(isSuccess, result.isSuccess(), "isSuccess powinno być zgodne z danymi testowymi.");
        assertEquals(message, result.getMessage(), "Wiadomość powinna być zgodna z danymi testowymi.");
    }
}
