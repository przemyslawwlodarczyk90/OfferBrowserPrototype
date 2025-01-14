//package com.example.offerbrowserprototype.domain.loginaandregister;
//
//import com.example.offerbrowserprototype.domain.dto.loginandregister.LoginDto;
//import com.example.offerbrowserprototype.domain.user.User;
//import com.example.offerbrowserprototype.infrastructure.repository.UserRepository;
//import com.example.offerbrowserprototype.infrastructure.security.JwtService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//
//class UserLoginHandlerTest {
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private PasswordEncoder passwordEncoder;
//
//    @Mock
//    private JwtService jwtService;
//
//    private UserLoginHandler userLoginHandler;
//
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        userLoginHandler = new UserLoginHandler(userRepository, passwordEncoder, jwtService);
//    }
//
//
//    @Test
//    void shouldReturnTokenWhenLoginIsSuccessful() {
//        // Given
//        LoginDto loginDto = new LoginDto();
//        loginDto.setUsername("kiepski");
//        loginDto.setPassword("paździoch");
//
//        User user = new User();
//        user.setUsername("kiepski");
//        user.setPassword("hashedPassword");
//
//        when(userRepository.findByUsername("kiepski")).thenReturn(Optional.of(user));
//        when(passwordEncoder.matches("paździoch", "hashedPassword")).thenReturn(true);
//        when(jwtService.generateToken(Mockito.any())).thenReturn("mockedToken");
//
//        // When
//        String token = userLoginHandler.login(loginDto);
//
//        // Then
//        assertEquals("mockedToken", token, "Token powinien być poprawnie wygenerowany.");
//        verify(jwtService, times(1)).generateToken(Mockito.any());
//    }
//
//
//    @Test
//    void shouldThrowExceptionWhenUsernameNotFound() {
//        // Given
//        LoginDto loginDto = new LoginDto();
//        loginDto.setUsername("nieznany");
//        loginDto.setPassword("paździoch");
//
//        when(userRepository.findByUsername("nieznany")).thenReturn(Optional.empty());
//
//        // When & Then
//        Exception exception = assertThrows(IllegalArgumentException.class, () -> userLoginHandler.login(loginDto));
//        assertEquals("Invalid username or password", exception.getMessage(),
//                "Powinna zostać zwrócona informacja o niepoprawnym loginie lub haśle.");
//        verify(jwtService, never()).generateToken(Mockito.any());
//    }
//
//
//    @Test
//    void shouldThrowExceptionWhenPasswordIsIncorrect() {
//        // Given
//        LoginDto loginDto = new LoginDto();
//        loginDto.setUsername("kiepski");
//        loginDto.setPassword("złyPaździoch");
//
//        User user = new User();
//        user.setUsername("kiepski");
//        user.setPassword("hashedPassword");
//
//        when(userRepository.findByUsername("kiepski")).thenReturn(Optional.of(user));
//        when(passwordEncoder.matches("złyPaździoch", "hashedPassword")).thenReturn(false);
//
//        // When & Then
//        Exception exception = assertThrows(IllegalArgumentException.class, () -> userLoginHandler.login(loginDto));
//        assertEquals("Invalid username or password", exception.getMessage(),
//                "Powinna zostać zwrócona informacja o niepoprawnym loginie lub haśle.");
//        verify(jwtService, never()).generateToken(Mockito.any());
//    }
//}
