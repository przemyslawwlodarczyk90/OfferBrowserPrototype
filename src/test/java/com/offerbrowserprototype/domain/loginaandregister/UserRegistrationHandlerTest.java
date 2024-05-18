package com.offerbrowserprototype.domain.loginaandregister;

import com.offerbrowserprototype.domain.exception.InvalidRegisterException;
import com.offerbrowserprototype.domain.loginaandregister.dto.RegisterUserDTO;
import com.offerbrowserprototype.domain.loginaandregister.dto.RegistrationResultDTO;
import com.offerbrowserprototype.domain.user.User;
import com.offerbrowserprototype.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserRegistrationHandlerTest {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private UserRegistrationHandler userRegistrationHandler;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        userRegistrationHandler = new UserRegistrationHandler(userRepository, passwordEncoder);
    }

    @Test
    void registerSuccessful() {
        RegisterUserDTO registerUserDTO = new RegisterUserDTO();
        registerUserDTO.setUsername("testUser");
        registerUserDTO.setEmail("test@example.com");
        registerUserDTO.setPassword("password");

        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");

        when(userRepository.findByUsername("testUser")).thenReturn(null);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        RegistrationResultDTO result = userRegistrationHandler.register(registerUserDTO);

        assertNotNull(result);
        assertEquals(1L, result.getUserId());
        assertEquals("testUser", result.getUsername());
        assertTrue(result.isSuccess());
        assertEquals("Registration successful", result.getMessage());
    }

    @Test
    void registerUsernameAlreadyExists() {
        RegisterUserDTO registerUserDTO = new RegisterUserDTO();
        registerUserDTO.setUsername("testUser");
        registerUserDTO.setEmail("test@example.com");
        registerUserDTO.setPassword("password");

        when(userRepository.findByUsername("testUser")).thenReturn(new User());

        InvalidRegisterException exception = assertThrows(InvalidRegisterException.class, () -> {
            userRegistrationHandler.register(registerUserDTO);
        });

        assertEquals("Username already exists", exception.getMessage());
    }
}