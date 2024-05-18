package com.offerbrowserprototype.domain.loginaandregister.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RegisterUserDTOTest {

    @Test
    public void testRegisterUserDTO() {
        String username = "testUser";
        String email = "test@example.com";
        String password = "testPassword";

        RegisterUserDTO registerUserDTO = new RegisterUserDTO();
        registerUserDTO.setUsername(username);
        registerUserDTO.setEmail(email);
        registerUserDTO.setPassword(password);

        assertEquals(username, registerUserDTO.getUsername());
        assertEquals(email, registerUserDTO.getEmail());
        assertEquals(password, registerUserDTO.getPassword());
    }
}