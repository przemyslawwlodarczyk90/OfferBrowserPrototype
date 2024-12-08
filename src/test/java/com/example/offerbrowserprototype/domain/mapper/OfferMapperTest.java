package com.example.offerbrowserprototype.domain.mapper;

import com.example.offerbrowserprototype.domain.dto.loginandregister.RegisterUserDTO;
import com.example.offerbrowserprototype.domain.dto.loginandregister.UpdateUserDto;
import com.example.offerbrowserprototype.domain.dto.user.UserDTO;
import com.example.offerbrowserprototype.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testy jednostkowe dla klasy {@link UserMapper}.
 * Klasa ta odpowiada za mapowanie obiektów {@link User} na różne DTO i odwrotnie.
 */
class UserMapperTest {

    private UserMapper userMapper;

    /**
     * Inicjalizacja obiektu {@link UserMapper} przed każdym testem.
     */
    @BeforeEach
    void setUp() {
        userMapper = new UserMapper();
    }

    /**
     * Test sprawdzający mapowanie obiektu {@link User} na {@link UserDTO}.
     */
    @Test
    void shouldMapUserToUserDTO() {
        // Given - Dane testowe
        User user = new User();
        user.setId("1");
        user.setUsername("jan_kowalski");
        user.setEmail("jan.kowalski@example.com");

        // When - Wywołanie metody
        UserDTO userDTO = userMapper.toDTO(user);

        // Then - Sprawdzenie wyników
        assertEquals(user.getId(), userDTO.getId(), "ID użytkownika powinno być takie samo");
        assertEquals(user.getUsername(), userDTO.getUsername(), "Nazwa użytkownika powinna być taka sama");
        assertEquals(user.getEmail(), userDTO.getEmail(), "Email użytkownika powinien być taki sam");
    }

    /**
     * Test sprawdzający mapowanie obiektu {@link RegisterUserDTO} na {@link User}.
     */
    @Test
    void shouldMapRegisterUserDTOToUser() {
        // Given - Dane testowe
        RegisterUserDTO registerUserDTO = new RegisterUserDTO();
        registerUserDTO.setUsername("jan_kowalski");
        registerUserDTO.setEmail("jan.kowalski@example.com");
        registerUserDTO.setPassword("password123");

        String hashedPassword = "hashedPassword123";

        // When - Wywołanie metody
        User user = userMapper.toEntity(registerUserDTO, hashedPassword);

        // Then - Sprawdzenie wyników
        assertEquals(registerUserDTO.getUsername(), user.getUsername(), "Nazwa użytkownika powinna być taka sama");
        assertEquals(registerUserDTO.getEmail(), user.getEmail(), "Email użytkownika powinien być taki sam");
        assertEquals(hashedPassword, user.getPassword(), "Hasło powinno być zahashowane");
    }

    /**
     * Test sprawdzający aktualizację obiektu {@link User} za pomocą {@link UpdateUserDto}.
     */
    @Test
    void shouldUpdateUserFromDto() {
        // Given - Dane testowe
        User user = new User();
        user.setUsername("old_username");
        user.setEmail("old_email@example.com");

        UpdateUserDto updateUserDto = new UpdateUserDto();
        updateUserDto.setUsername("new_username");
        updateUserDto.setEmail("new_email@example.com");

        // When - Wywołanie metody
        userMapper.updateUserFromDto(updateUserDto, user);

        // Then - Sprawdzenie wyników
        assertEquals(updateUserDto.getUsername(), user.getUsername(), "Nazwa użytkownika powinna zostać zaktualizowana");
        assertEquals(updateUserDto.getEmail(), user.getEmail(), "Email użytkownika powinien zostać zaktualizowany");
    }

    /**
     * Test sprawdzający, że puste pola w {@link UpdateUserDto} nie nadpisują wartości w obiekcie {@link User}.
     */
    @Test
    void shouldNotUpdateUserWithEmptyFields() {
        // Given - Dane testowe
        User user = new User();
        user.setUsername("old_username");
        user.setEmail("old_email@example.com");

        UpdateUserDto updateUserDto = new UpdateUserDto();
        updateUserDto.setUsername(""); // Puste pole
        updateUserDto.setEmail(null); // Null

        // When - Wywołanie metody
        userMapper.updateUserFromDto(updateUserDto, user);

        // Then - Sprawdzenie wyników
        assertEquals("old_username", user.getUsername(), "Nazwa użytkownika nie powinna zostać zmieniona");
        assertEquals("old_email@example.com", user.getEmail(), "Email użytkownika nie powinien zostać zmieniony");
    }
}
