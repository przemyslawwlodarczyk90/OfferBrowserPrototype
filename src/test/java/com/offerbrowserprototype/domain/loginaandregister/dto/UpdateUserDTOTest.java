package com.offerbrowserprototype.domain.loginaandregister.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UpdateUserDTOTest {

    @Test
    public void testUpdateUserDTO() {
        Long id = 1L;
        String username = "testUser";
        String email = "test@example.com";

        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setId(id);
        updateUserDTO.setUsername(username);
        updateUserDTO.setEmail(email);

        assertEquals(id, updateUserDTO.getId());
        assertEquals(username, updateUserDTO.getUsername());
        assertEquals(email, updateUserDTO.getEmail());
    }
}