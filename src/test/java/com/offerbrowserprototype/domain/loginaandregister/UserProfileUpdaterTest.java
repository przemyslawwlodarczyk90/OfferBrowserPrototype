package com.offerbrowserprototype.domain.loginaandregister;

import com.offerbrowserprototype.domain.exception.UserNotFoundException;
import com.offerbrowserprototype.domain.loginaandregister.dto.UpdateUserDTO;
import com.offerbrowserprototype.domain.user.User;
import com.offerbrowserprototype.domain.user.UserDTO;
import com.offerbrowserprototype.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserProfileUpdaterTest {

    private UserRepository userRepository;
    private UserProfileUpdater userProfileUpdater;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userProfileUpdater = new UserProfileUpdater(userRepository);
    }

    @Test
    void updateUserProfileSuccessful() {
        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setId(1L);
        updateUserDTO.setUsername("newUsername");
        updateUserDTO.setEmail("newEmail@example.com");

        User user = new User();
        user.setId(1L);
        user.setUsername("oldUsername");
        user.setEmail("oldEmail@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        UserDTO result = userProfileUpdater.updateUserProfile(updateUserDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("newUsername", result.getUsername());
        assertEquals("newEmail@example.com", result.getEmail());
    }

    @Test
    void updateUserProfileUserNotFound() {
        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setId(1L);
        updateUserDTO.setUsername("newUsername");
        updateUserDTO.setEmail("newEmail@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userProfileUpdater.updateUserProfile(updateUserDTO);
        });

        assertEquals("User not found", exception.getMessage());
    }
}