//package com.example.offerbrowserprototype.domain.loginaandregister;
//
//import com.example.offerbrowserprototype.domain.loginaandregister.dto.RegisterUserDTO;
//import com.example.offerbrowserprototype.domain.user.User;
//import com.example.offerbrowserprototype.domain.user.UserRepository;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class UserRegistrationHandlerTest {
//
//    @Test
//    void shouldRegisterNewUserWithHashedPassword() {
//        // given
//        UserRepository userRepository = Mockito.mock(UserRepository.class);
//        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
//        UserRegistrationHandler handler = new UserRegistrationHandler(userRepository, passwordEncoder);
//
//        RegisterUserDTO userDto = new RegisterUserDTO();
//        userDto.setUsername("newuser");
//        userDto.setEmail("newuser@example.com");
//        userDto.setPassword("password123");
//
//        when(userRepository.findByUsername("newuser")).thenReturn(Optional.empty());
//        when(passwordEncoder.encode("password123")).thenReturn("hashed_password");
//
//        // when
//        handler.register(userDto);
//
//        // then
//        verify(userRepository).save(any(User.class));
//        verify(passwordEncoder).encode("password123");
//    }
//
//    @Test
//    void shouldReturnErrorWhenUsernameAlreadyExists() {
//        // given
//        UserRepository userRepository = Mockito.mock(UserRepository.class);
//        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
//        UserRegistrationHandler handler = new UserRegistrationHandler(userRepository, passwordEncoder);
//
//        RegisterUserDTO userDto = new RegisterUserDTO();
//        userDto.setUsername("existinguser");
//        userDto.setEmail("newuser@example.com");
//        userDto.setPassword("password123");
//
//        when(userRepository.findByUsername("existinguser")).thenReturn(Optional.of(new User()));
//
//        // when
//        var result = handler.register(userDto);
//
//        // then
//        assertFalse(result.isSuccess());
//        assertEquals("Username already taken", result.getMessage());
//        verify(userRepository, never()).save(any(User.class));
//    }
//}
