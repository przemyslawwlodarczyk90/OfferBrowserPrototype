package com.example.offerbrowserprototype.domain.loginaandregister;

import com.example.offerbrowserprototype.domain.dto.loginandregister.UpdateUserDto;
import com.example.offerbrowserprototype.domain.dto.user.UserDTO;
import com.example.offerbrowserprototype.domain.mapper.UserMapper;
import com.example.offerbrowserprototype.domain.user.User;
import com.example.offerbrowserprototype.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


class UserProfileUpdaterTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    private UserProfileUpdater userProfileUpdater;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userProfileUpdater = new UserProfileUpdater(userRepository, userMapper);
    }


    @Test
    void shouldUpdateUserProfileSuccessfully() {
        // Given
        UpdateUserDto updateUserDto = new UpdateUserDto("1", "newUsername", "newEmail@example.com");
        User existingUser = new User();
        existingUser.setId("1");
        existingUser.setUsername("oldUsername");
        existingUser.setEmail("oldEmail@example.com");

        User updatedUser = new User();
        updatedUser.setId("1");
        updatedUser.setUsername("newUsername");
        updatedUser.setEmail("newEmail@example.com");

        UserDTO updatedUserDTO = new UserDTO("1", "newUsername", "newEmail@example.com");

        when(userRepository.findById("1")).thenReturn(Optional.of(existingUser));
        doNothing().when(userMapper).updateUserFromDto(updateUserDto, existingUser);
        when(userRepository.save(existingUser)).thenReturn(updatedUser);
        when(userMapper.toDTO(updatedUser)).thenReturn(updatedUserDTO);

        // When
        UserDTO result = userProfileUpdater.updateUserProfile(updateUserDto);

        // Then
        assertEquals(updatedUserDTO, result, "Zaktualizowany profil użytkownika powinien być poprawny.");
        verify(userRepository, times(1)).findById("1");
        verify(userRepository, times(1)).save(existingUser);
        verify(userMapper, times(1)).updateUserFromDto(updateUserDto, existingUser);
        verify(userMapper, times(1)).toDTO(updatedUser);
    }


    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        // Given
        UpdateUserDto updateUserDto = new UpdateUserDto("nonexistent", "username", "email@example.com");
        when(userRepository.findById("nonexistent")).thenReturn(Optional.empty());

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userProfileUpdater.updateUserProfile(updateUserDto),
                "Metoda powinna rzucić wyjątek, gdy użytkownik nie istnieje.");

        assertEquals("User not found", exception.getMessage(), "Treść wyjątku powinna być zgodna.");
        verify(userRepository, times(1)).findById("nonexistent");
        verify(userRepository, never()).save(any());
        verify(userMapper, never()).updateUserFromDto(any(), any());
        verify(userMapper, never()).toDTO(any());
    }
}
