package com.offerbrowserprototype.domain.user;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    public void testNoArgsConstructor() {
        User user = new User();
        assertNotNull(user);
    }

    @Test
    public void testAllArgsConstructor() {
        Long id = 1L;
        String username = "testUser";
        String email = "test@example.com";
        String password = "password";

        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);

        assertEquals(id, user.getId());
        assertEquals(username, user.getUsername());
        assertEquals(email, user.getEmail());
        assertEquals(password, user.getPassword());
    }

    @Test
    public void testSettersAndGetters() {
        Long id = 1L;
        String username = "testUser";
        String email = "test@example.com";
        String password = "password";

        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);

        assertEquals(id, user.getId());
        assertEquals(username, user.getUsername());
        assertEquals(email, user.getEmail());
        assertEquals(password, user.getPassword());
    }

    @Test
    public void testEqualsAndHashCode() {
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("testUser");
        user1.setEmail("test@example.com");
        user1.setPassword("password");

        User user2 = new User();
        user2.setId(1L);
        user2.setUsername("testUser");
        user2.setEmail("test@example.com");
        user2.setPassword("password");

        User user3 = new User();
        user3.setId(2L);
        user3.setUsername("anotherUser");
        user3.setEmail("another@example.com");
        user3.setPassword("anotherPassword");

        assertEquals(user1, user2);
        assertNotEquals(user1, user3);

        assertEquals(user1.hashCode(), user2.hashCode());
        assertNotEquals(user1.hashCode(), user3.hashCode());
    }

    @Test
    public void testToString() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");
        user.setEmail("test@example.com");
        user.setPassword("password");

        String expectedString = "User(id=1, username=testUser, email=test@example.com, password=password)";
        assertEquals(expectedString, user.toString());
    }
}