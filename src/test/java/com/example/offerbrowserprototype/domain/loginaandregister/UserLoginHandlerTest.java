package com.example.offerbrowserprototype.domain.loginaandregister;

import com.example.offerbrowserprototype.domain.dto.loginandregister.LoginDto;
import com.example.offerbrowserprototype.domain.exception.AccountNotActivatedException;
import com.example.offerbrowserprototype.domain.user.User;
import com.example.offerbrowserprototype.infrastructure.repository.UserRepository;
import com.example.offerbrowserprototype.infrastructure.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

// Testy jednostkowe handlera logowania użytkownika przez JWT
@ExtendWith(MockitoExtension.class)
class UserLoginHandlerTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtService jwtService;
    @InjectMocks private UserLoginHandler handler;

    @Test
    void loguje_uzytkownika_i_zwraca_token_z_danymi() {
        // given
        LoginDto loginDto = new LoginDto();
        loginDto.setUsername("jan");
        loginDto.setPassword("haslo123");
        User user = new User();
        user.setId(1L);
        user.setUsername("jan");
        user.setEmail("jan@test.com");
        user.setPassword("zakodowane");
        user.setActive(true);

        when(userRepository.findByUsername("jan")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("haslo123", "zakodowane")).thenReturn(true);
        when(jwtService.generateToken(any())).thenReturn("token.jwt.value");

        // when
        UserLoginHandler.LoginResult result = handler.login(loginDto);

        // then — wynik zawiera token i dane profilu użytkownika
        assertThat(result.token).isEqualTo("token.jwt.value");
        assertThat(result.userId).isEqualTo(1L);
        assertThat(result.username).isEqualTo("jan");
        assertThat(result.email).isEqualTo("jan@test.com");
    }

    @Test
    void blokuje_logowanie_na_niepotwierdzone_konto() {
        // given — poprawne hasło, ale rejestracja niepotwierdzona mailem
        User user = new User();
        user.setUsername("nowy");
        user.setPassword("zakodowane");
        user.setActive(false);
        when(userRepository.findByUsername("nowy")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("haslo", "zakodowane")).thenReturn(true);

        LoginDto dto = new LoginDto();
        dto.setUsername("nowy");
        dto.setPassword("haslo");

        // when / then — brak tokenu
        assertThatThrownBy(() -> handler.login(dto))
                .isInstanceOf(AccountNotActivatedException.class)
                .hasMessageContaining("nie zostało potwierdzone");
        org.mockito.Mockito.verifyNoInteractions(jwtService);
    }

    @Test
    void niepotwierdzone_konto_ze_zlym_haslem_dostaje_zwykly_blad_logowania() {
        User user = new User();
        user.setUsername("nowy");
        user.setPassword("zakodowane");
        user.setActive(false);
        when(userRepository.findByUsername("nowy")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("zle", "zakodowane")).thenReturn(false);

        LoginDto dto = new LoginDto();
        dto.setUsername("nowy");
        dto.setPassword("zle");

        assertThatThrownBy(() -> handler.login(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid username or password");
    }

    @Test
    void rzuca_wyjatek_gdy_uzytkownik_nie_istnieje() {
        // given
        when(userRepository.findByUsername("nieznany")).thenReturn(Optional.empty());

        // when / then
        LoginDto nieznanyDto = new LoginDto();
        nieznanyDto.setUsername("nieznany");
        nieznanyDto.setPassword("haslo");
        assertThatThrownBy(() -> handler.login(nieznanyDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid username or password");
    }

    @Test
    void rzuca_wyjatek_gdy_haslo_jest_nieprawidlowe() {
        // given
        User user = new User();
        user.setUsername("jan");
        user.setPassword("zakodowane");
        when(userRepository.findByUsername("jan")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("zle_haslo", "zakodowane")).thenReturn(false);

        // when / then
        LoginDto zleHasloDto = new LoginDto();
        zleHasloDto.setUsername("jan");
        zleHasloDto.setPassword("zle_haslo");
        assertThatThrownBy(() -> handler.login(zleHasloDto))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
