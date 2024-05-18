package com.offerbrowserprototype.domain.loginaandregister;

import com.offerbrowserprototype.domain.exception.AuthorizationException;
import com.offerbrowserprototype.domain.loginaandregister.dto.LoginDTO;
import com.offerbrowserprototype.domain.user.User;
import com.offerbrowserprototype.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserLoginHandlerTest {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private UserLoginHandler userLoginHandler;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        userLoginHandler = new UserLoginHandler(userRepository, passwordEncoder);
    }

    @Test
    void loginSuccessful() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("testUser");
        loginDTO.setPassword("testPassword");

        User user = new User();
        user.setUsername("testUser");
        user.setPassword("encodedPassword");

        when(userRepository.findByUsername("testUser")).thenReturn(user);
        when(passwordEncoder.matches("testPassword", "encodedPassword")).thenReturn(true);

        String token = userLoginHandler.login(loginDTO);

        assertNotNull(token);
        assertEquals("token_jwt", token);
    }

    @Test
    void loginUserNotFound() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("testUser");
        loginDTO.setPassword("testPassword");

        when(userRepository.findByUsername("testUser")).thenReturn(null);

        AuthorizationException exception = assertThrows(AuthorizationException.class, () -> {
            userLoginHandler.login(loginDTO);
        });

        assertEquals("Invalid username or password", exception.getMessage());
    }

    @Test
    void loginInvalidPassword() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("testUser");
        loginDTO.setPassword("wrongPassword");

        User user = new User();
        user.setUsername("testUser");
        user.setPassword("encodedPassword");

        when(userRepository.findByUsername("testUser")).thenReturn(user);
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        AuthorizationException exception = assertThrows(AuthorizationException.class, () -> {
            userLoginHandler.login(loginDTO);
        });

        assertEquals("Invalid username or password", exception.getMessage());
    }
}
