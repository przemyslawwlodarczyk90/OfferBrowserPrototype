package com.offerbrowserprototype.domain.user;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserDTOTest {

    @Test
    public void testNoArgsConstructor() {
        UserDTO userDTO = new UserDTO();
        assertNotNull(userDTO);
    }

    @Test
    public void testAllArgsConstructor() {
        Long id = 1L;
        String username = "testUser";
        String email = "test@example.com";

        UserDTO userDTO = new UserDTO(id, username, email);

        assertEquals(id, userDTO.getId());
        assertEquals(username, userDTO.getUsername());
        assertEquals(email, userDTO.getEmail());
    }

    @Test
    public void testSettersAndGetters() {
        Long id = 1L;
        String username = "testUser";
        String email = "test@example.com";

        UserDTO userDTO = new UserDTO();
        userDTO.setId(id);
        userDTO.setUsername(username);
        userDTO.setEmail(email);

        assertEquals(id, userDTO.getId());
        assertEquals(username, userDTO.getUsername());
        assertEquals(email, userDTO.getEmail());
    }

    @Test
    public void testEqualsAndHashCode() {
        UserDTO userDTO1 = new UserDTO(1L, "testUser", "test@example.com");
        UserDTO userDTO2 = new UserDTO(1L, "testUser", "test@example.com");
        UserDTO userDTO3 = new UserDTO(2L, "anotherUser", "another@example.com");

        assertEquals(userDTO1, userDTO2);
        assertNotEquals(userDTO1, userDTO3);

        assertEquals(userDTO1.hashCode(), userDTO2.hashCode());
        assertNotEquals(userDTO1.hashCode(), userDTO3.hashCode());
    }

    @Test
    public void testToString() {
        UserDTO userDTO = new UserDTO(1L, "testUser", "test@example.com");
        String expectedString = "UserDTO(id=1, username=testUser, email=test@example.com)";

        assertEquals(expectedString, userDTO.toString());
    }
}