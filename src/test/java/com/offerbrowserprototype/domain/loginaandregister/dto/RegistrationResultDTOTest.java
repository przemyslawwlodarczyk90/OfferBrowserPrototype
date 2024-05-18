package com.offerbrowserprototype.domain.loginaandregister.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RegistrationResultDTOTest {

    @Test
    public void testNoArgsConstructor() {
        RegistrationResultDTO dto = new RegistrationResultDTO();
        assertNotNull(dto);
    }

    @Test
    public void testAllArgsConstructor() {
        Long userId = 1L;
        String username = "testUser";
        boolean isSuccess = true;
        String message = "Registration successful";

        RegistrationResultDTO dto = new RegistrationResultDTO(userId, username, isSuccess, message);

        assertEquals(userId, dto.getUserId());
        assertEquals(username, dto.getUsername());
        assertTrue(dto.isSuccess());
        assertEquals(message, dto.getMessage());
    }

    @Test
    public void testSettersAndGetters() {
        Long userId = 1L;
        String username = "testUser";
        boolean isSuccess = true;
        String message = "Registration successful";

        RegistrationResultDTO dto = new RegistrationResultDTO();
        dto.setUserId(userId);
        dto.setUsername(username);
        dto.setSuccess(isSuccess);
        dto.setMessage(message);

        assertEquals(userId, dto.getUserId());
        assertEquals(username, dto.getUsername());
        assertTrue(dto.isSuccess());
        assertEquals(message, dto.getMessage());
    }
}