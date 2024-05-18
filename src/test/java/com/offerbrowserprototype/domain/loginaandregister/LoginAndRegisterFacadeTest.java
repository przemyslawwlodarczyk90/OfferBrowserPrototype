package com.offerbrowserprototype.domain.loginaandregister;

import com.offerbrowserprototype.domain.loginaandregister.dto.*;
import com.offerbrowserprototype.domain.user.UserDTO;
import com.offerbrowserprototype.infrastructure.security.jwt.JwtTokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoginAndRegisterFacadeTest {

    private UserRegistrationHandler registrationHandler;
    private UserLoginHandler loginHandler;
    private UserProfileUpdater profileUpdater;
    private UserPasswordChanger passwordChanger;
    private JwtTokenUtil jwtTokenUtil;
    private LoginAndRegisterFacade loginAndRegisterFacade;

    @BeforeEach
    void setUp() {
        registrationHandler = mock(UserRegistrationHandler.class);
        loginHandler = mock(UserLoginHandler.class);
        profileUpdater = mock(UserProfileUpdater.class);
        passwordChanger = mock(UserPasswordChanger.class);
        jwtTokenUtil = mock(JwtTokenUtil.class);
        loginAndRegisterFacade = new LoginAndRegisterFacade(registrationHandler, loginHandler, profileUpdater, passwordChanger, jwtTokenUtil);
    }

    @Test
    void register() {
        RegisterUserDTO registerUserDTO = new RegisterUserDTO();
        registerUserDTO.setUsername("testUser");
        registerUserDTO.setEmail("test@example.com");
        registerUserDTO.setPassword("password");

        RegistrationResultDTO expectedResult = new RegistrationResultDTO(1L, "testUser", true, "Registration successful");
        when(registrationHandler.register(registerUserDTO)).thenReturn(expectedResult);

        RegistrationResultDTO result = loginAndRegisterFacade.register(registerUserDTO);

        assertNotNull(result);
        assertEquals(expectedResult, result);
        verify(registrationHandler).register(registerUserDTO);
    }

    @Test
    void login() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("testUser");
        loginDTO.setPassword("password");

        when(jwtTokenUtil.generateToken("testUser")).thenReturn("token_jwt");

        String token = loginAndRegisterFacade.login(loginDTO);

        assertNotNull(token);
        assertEquals("token_jwt", token);
        verify(jwtTokenUtil).generateToken("testUser");
    }

    @Test
    void updateUserProfile() {
        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setId(1L);
        updateUserDTO.setUsername("updatedUser");
        updateUserDTO.setEmail("updated@example.com");

        UserDTO expectedUserDTO = new UserDTO(1L, "updatedUser", "updated@example.com");
        when(profileUpdater.updateUserProfile(updateUserDTO)).thenReturn(expectedUserDTO);

        UserDTO result = loginAndRegisterFacade.updateUserProfile(updateUserDTO);

        assertNotNull(result);
        assertEquals(expectedUserDTO, result);
        verify(profileUpdater).updateUserProfile(updateUserDTO);
    }

    @Test
    void changeUserPassword() {
        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO();
        changePasswordDTO.setId(1L);
        changePasswordDTO.setOldPassword("oldPassword");
        changePasswordDTO.setNewPassword("newPassword");

        when(passwordChanger.changeUserPassword(changePasswordDTO)).thenReturn(true);

        boolean result = loginAndRegisterFacade.changeUserPassword(changePasswordDTO);

        assertTrue(result);
        verify(passwordChanger).changeUserPassword(changePasswordDTO);
    }
}
