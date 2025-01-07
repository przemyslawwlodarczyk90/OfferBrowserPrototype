package com.example.offerbrowserprototype.domain.loginaandregister;

import com.example.offerbrowserprototype.domain.dto.loginandregister.ChangePasswordDto;
import com.example.offerbrowserprototype.domain.user.User;
import com.example.offerbrowserprototype.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserPasswordChangerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserPasswordChanger userPasswordChanger;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userPasswordChanger = new UserPasswordChanger(userRepository, passwordEncoder);
    }


    @Test
    void shouldChangePasswordWhenCurrentPasswordIsCorrect() {
        // Given
        ChangePasswordDto changePasswordDto = new ChangePasswordDto();
        changePasswordDto.setUsername("kiepski");
        changePasswordDto.setCurrentPassword("paździoch");
        changePasswordDto.setNewPassword("noweHasło");

        User user = new User();
        user.setUsername("kiepski");
        user.setPassword("hashedPassword");

        when(userRepository.findByUsername("kiepski")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("paździoch", "hashedPassword")).thenReturn(true);
        when(passwordEncoder.encode("noweHasło")).thenReturn("encodedNoweHasło");

        // When
        boolean result = userPasswordChanger.changeUserPassword(changePasswordDto);

        // Then
        assertTrue(result, "Zmiana hasła powinna się powieść.");
        assertEquals("encodedNoweHasło", user.getPassword(), "Hasło użytkownika powinno zostać zaktualizowane.");
        verify(userRepository, times(1)).save(user);
    }


    @Test
    void shouldNotChangePasswordWhenCurrentPasswordIsIncorrect() {
        // Given
        ChangePasswordDto changePasswordDto = new ChangePasswordDto();
        changePasswordDto.setUsername("kiepski");
        changePasswordDto.setCurrentPassword("złyPaździoch");
        changePasswordDto.setNewPassword("noweHasło");

        User user = new User();
        user.setUsername("kiepski");
        user.setPassword("hashedPassword");

        when(userRepository.findByUsername("kiepski")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("złyPaździoch", "hashedPassword")).thenReturn(false);

        // When
        boolean result = userPasswordChanger.changeUserPassword(changePasswordDto);

        // Then
        assertFalse(result, "Zmiana hasła nie powinna się powieść przy niepoprawnym obecnym haśle.");
        verify(userRepository, never()).save(user);
    }


    @Test
    void shouldNotChangePasswordWhenUserNotFound() {
        // Given
        ChangePasswordDto changePasswordDto = new ChangePasswordDto();
        changePasswordDto.setUsername("nieistniejący");
        changePasswordDto.setCurrentPassword("paździoch");
        changePasswordDto.setNewPassword("noweHasło");

        when(userRepository.findByUsername("nieistniejący")).thenReturn(Optional.empty());

        // When
        boolean result = userPasswordChanger.changeUserPassword(changePasswordDto);

        // Then
        assertFalse(result, "Zmiana hasła nie powinna się powieść, gdy użytkownik nie istnieje.");
        verify(userRepository, never()).save(any());
    }
}
