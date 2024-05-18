package com.offerbrowserprototype.domain.loginaandregister.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoginDTOTest {

    @Test
    public void testLoginDTO() {
        String username = "testUser";
        String password = "testPassword";

        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername(username);
        loginDTO.setPassword(password);

        assertEquals(username, loginDTO.getUsername());
        assertEquals(password, loginDTO.getPassword());
    }
}