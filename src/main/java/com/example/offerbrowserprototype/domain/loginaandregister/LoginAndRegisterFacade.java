package com.example.offerbrowserprototype.domain.loginaandregister;

import com.example.offerbrowserprototype.domain.dto.loginandregister.*;


import com.example.offerbrowserprototype.domain.user.ConfirmationToken;
import com.example.offerbrowserprototype.domain.user.User;
import com.example.offerbrowserprototype.domain.dto.user.UserDTO;

import com.example.offerbrowserprototype.infrastructure.repository.UserRepository;
import com.example.offerbrowserprototype.infrastructure.service.ConfirmationTokenService;
import org.springframework.stereotype.Component;

@Component
public class LoginAndRegisterFacade {

    private final UserRegistrationHandler registrationHandler;
    private final UserLoginHandler loginHandler;
    private final UserProfileUpdater profileUpdater;
    private final ConfirmationTokenService confirmationTokenService;
    private final UserPasswordChanger passwordChanger;
    private final UserRepository userRepository;

    public LoginAndRegisterFacade(UserRegistrationHandler registrationHandler,
                                  UserLoginHandler loginHandler,
                                  UserProfileUpdater profileUpdater,
                                  ConfirmationTokenService confirmationTokenService, UserPasswordChanger passwordChanger, UserRepository userRepository) {
        this.registrationHandler = registrationHandler;
        this.loginHandler = loginHandler;
        this.profileUpdater = profileUpdater;
        this.confirmationTokenService = confirmationTokenService;
        this.passwordChanger = passwordChanger;
        this.userRepository = userRepository;
    }

    public RegistrationResultDTO register(RegisterUserDTO userDto) {
        return registrationHandler.register(userDto);
    }

    public String login(LoginDto loginDto) {
        return loginHandler.login(loginDto);
    }

    public UserDTO updateUserProfile(UpdateUserDto updateUserDto) {
        return profileUpdater.updateUserProfile(updateUserDto);
    }

    public boolean changeUserPassword(ChangePasswordDto changePasswordDto) {
        return passwordChanger.changeUserPassword(changePasswordDto);
    }
    public String confirmRegistration(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService.getToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));

        // Potwierdzenie tokena i aktywacja użytkownika
        confirmationTokenService.confirmToken(confirmationToken);
        activateUser(confirmationToken.getUserId());

        return "Registration confirmed!";
    }

    private void activateUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.setActive(true); // Ustawienie pola 'active' na true, aby aktywować użytkownika
        userRepository.save(user);
    }



}