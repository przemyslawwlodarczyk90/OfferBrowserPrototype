package com.example.offerbrowserprototype.domain.mapper;

import com.example.offerbrowserprototype.domain.dto.loginandregister.RegisterUserDTO;
import com.example.offerbrowserprototype.domain.dto.loginandregister.UpdateUserDto;
import com.example.offerbrowserprototype.domain.dto.user.UserDTO;
import com.example.offerbrowserprototype.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.StringUtils;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testy jednostkowe dla klasy UserMapper.
 * Klasa ta zajmuje się mapowaniem obiektów domenowych na obiekty DTO i odwrotnie.
 */
class UserMapperTest {

    private UserMapper userMapper;

    /**
     * Przygotowanie środowiska testowego przed każdym testem.
     */
    @BeforeEach
    void setUp() {
        userMapper = new UserMapper();
    }

    /**
     * Test mapowania obiektu `User` na `UserDTO`.
     */
    @Test
    void shouldMapUserToUserDTO() {
        // Given - Dane testowe
        User user = new User();
        user.setId("123");
        user.setUsername("waldek_kiepski");
        user.setEmail("waldek@example.com");

        // When - Wywołanie metody mapującej
        UserDTO userDTO = userMapper.toDTO(user);

        // Then - Weryfikacja wyników
        assertEquals(user.getId(), userDTO.getId());
        assertEquals(user.getUsername(), userDTO.getUsername());
        assertEquals(user.getEmail(), userDTO.getEmail());
    }

    /**
     * Test mapowania obiektu `RegisterUserDTO` na `User`.
     */
    @Test
    void shouldMapRegisterUserDTOToUser() {
        // Given - Dane testowe
        RegisterUserDTO registerUserDTO = new RegisterUserDTO();
        registerUserDTO.setUsername("waldek_kiepski");
        registerUserDTO.setEmail("waldek@example.com");
        registerUserDTO.setPassword("password123");
        String hashedPassword = "hashedPassword123";

        // When - Wywołanie metody mapującej
        User user = userMapper.toEntity(registerUserDTO, hashedPassword);

        // Then - Weryfikacja wyników
        assertEquals(registerUserDTO.getUsername(), user.getUsername());
        assertEquals(registerUserDTO.getEmail(), user.getEmail());
        assertEquals(hashedPassword, user.getPassword());
    }

    /**
     * Test aktualizacji obiektu `User` za pomocą `UpdateUserDto`.
     */
    @Test
    void shouldUpdateUserFromDto() {
        // Given - Dane testowe
        UpdateUserDto updateUserDto = new UpdateUserDto();
        updateUserDto.setUsername("new_username");
        updateUserDto.setEmail("new_email@example.com");

        User user = new User();
        user.setUsername("old_username");
        user.setEmail("old_email@example.com");

        // When - Wywołanie metody aktualizującej
        userMapper.updateUserFromDto(updateUserDto, user);

        // Then - Weryfikacja wyników
        assertEquals(updateUserDto.getUsername(), user.getUsername());
        assertEquals(updateUserDto.getEmail(), user.getEmail());
    }

    /**
     * Test zapewniający, że puste lub `null` wartości w `UpdateUserDto` nie nadpisują danych w `User`.
     */
    @Test
    void shouldNotUpdateUserWhenFieldsAreEmptyInUpdateUserDto() {
        // Given - Dane testowe
        UpdateUserDto updateUserDto = new UpdateUserDto();
        updateUserDto.setUsername("");
        updateUserDto.setEmail(null);

        User user = new User();
        user.setUsername("old_username");
        user.setEmail("old_email@example.com");

        // When - Wywołanie metody aktualizującej
        userMapper.updateUserFromDto(updateUserDto, user);

        // Then - Weryfikacja wyników
        assertEquals("old_username", user.getUsername());
        assertEquals("old_email@example.com", user.getEmail());
    }
}
