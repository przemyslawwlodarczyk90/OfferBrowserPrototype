package com.example.offerbrowserprototype.domain.user;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {


    @Test
    void shouldCreateUserUsingDefaultConstructor() {

        User user = new User();


        assertNull(user.getId());
        assertNull(user.getUsername());
        assertNull(user.getEmail());
        assertNull(user.getPassword());
        assertFalse(user.isActive());
    }


    @Test
    void shouldSetAndGetId() {

        User user = new User();
        String id = "user-id";


        user.setId(id);

        assertEquals(id, user.getId());
    }

    @Test
    void shouldSetAndGetUsername() {

        User user = new User();
        String username = "janek_kowalski";

        user.setUsername(username);

        assertEquals(username, user.getUsername());
    }


    @Test
    void shouldSetAndGetEmail() {

        User user = new User();
        String email = "janek@example.com";

        user.setEmail(email);

        assertEquals(email, user.getEmail());
    }


    @Test
    void shouldSetAndGetPassword() {
        User user = new User();
        String password = "securepassword";

        user.setPassword(password);

        assertEquals(password, user.getPassword());
    }


    @Test
    void shouldSetAndGetActive() {
        User user = new User();
        boolean active = true;

        user.setActive(active);

        assertTrue(user.isActive());
    }
}
