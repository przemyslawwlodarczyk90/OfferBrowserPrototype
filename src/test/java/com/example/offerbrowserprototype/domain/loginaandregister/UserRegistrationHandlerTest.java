//package com.example.offerbrowserprototype.domain.loginaandregister;
//
//import com.example.offerbrowserprototype.domain.dto.loginandregister.RegisterUserDTO;
//import com.example.offerbrowserprototype.domain.dto.loginandregister.RegistrationResultDTO;
//import com.example.offerbrowserprototype.infrastructure.repository.UserRepository;
//import com.example.offerbrowserprototype.infrastructure.service.ConfirmationTokenService;
//import com.example.offerbrowserprototype.infrastructure.service.MailService;
//import com.example.offerbrowserprototype.domain.mapper.UserMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.time.Clock;
//import java.time.Instant;
//import java.time.ZoneId;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//
//class UserRegistrationHandlerTest {
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private PasswordEncoder passwordEncoder;
//
//    @Mock
//    private UserMapper userMapper;
//
//    @Mock
//    private MailService mailService;
//
//    @Mock
//    private ConfirmationTokenService confirmationTokenService;
//
//    private Clock fixedClock;
//
//    private UserRegistrationHandler userRegistrationHandler;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//
//        // Tworzenie Clock z ustawioną strefą czasową
//        fixedClock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
//
//        userRegistrationHandler = new UserRegistrationHandler(
//                userRepository,
//                passwordEncoder,
//                userMapper,
//                mailService,
//                confirmationTokenService,
//                fixedClock
//        );
//    }
//
//    @Test
//    void shouldRegisterUserSuccessfully() {
//        // Given - Dane testowe
//        RegisterUserDTO registerUserDTO = new RegisterUserDTO();
//        registerUserDTO.setUsername("waldek_kiepski");
//        registerUserDTO.setEmail("waldek@example.com");
//        registerUserDTO.setPassword("password123");
//
//        Mockito.when(userRepository.findByUsername("waldek_kiepski")).thenReturn(java.util.Optional.empty());
//        Mockito.when(passwordEncoder.encode("password123")).thenReturn("hashedPassword");
//        Mockito.when(userMapper.toEntity(any(), any())).thenReturn(new com.example.offerbrowserprototype.domain.user.User());
//        Mockito.doNothing().when(mailService).sendConfirmationEmail(any(), any(), any(), any());
//        Mockito.doNothing().when(confirmationTokenService).saveConfirmationToken(any());
//
//        // When
//        RegistrationResultDTO result = userRegistrationHandler.register(registerUserDTO);
//
//        // Then
//        assertEquals("waldek_kiepski", result.getUsername());
//        assertEquals(true, result.isSuccess());
//        assertEquals("Rejestracja udana", result.getMessage());
//    }
//
//    @Test
//    void shouldFailWhenUsernameAlreadyTaken() {
//        // Given
//        RegisterUserDTO registerUserDTO = new RegisterUserDTO();
//        registerUserDTO.setUsername("waldek_kiepski");
//        registerUserDTO.setEmail("waldek@example.com");
//        registerUserDTO.setPassword("password123");
//
//        Mockito.when(userRepository.findByUsername("waldek_kiepski")).thenReturn(java.util.Optional.of(new com.example.offerbrowserprototype.domain.user.User()));
//
//        // When
//        RegistrationResultDTO result = userRegistrationHandler.register(registerUserDTO);
//
//        // Then
//        assertEquals("waldek_kiepski", result.getUsername());
//        assertEquals(false, result.isSuccess());
//        assertEquals("Username already taken", result.getMessage());
//    }
//}
