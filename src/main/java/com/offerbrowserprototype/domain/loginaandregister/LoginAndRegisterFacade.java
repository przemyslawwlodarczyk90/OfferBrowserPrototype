package com.offerbrowserprototype.domain.loginaandregister;

import com.offerbrowserprototype.domain.loginaandregister.dto.*;
import com.offerbrowserprototype.domain.user.UserDto;
import org.springframework.stereotype.Component;

@Component
public class LoginAndRegisterFacade {

    private final UserRegistrationHandler registrationHandler;
    private final UserLoginHandler loginHandler;
    private final UserProfileUpdater profileUpdater;
    private final UserPasswordChanger passwordChanger;

    public LoginAndRegisterFacade(UserRegistrationHandler registrationHandler,
                                  UserLoginHandler loginHandler,
                                  UserProfileUpdater profileUpdater,
                                  UserPasswordChanger passwordChanger) {
        this.registrationHandler = registrationHandler;
        this.loginHandler = loginHandler;
        this.profileUpdater = profileUpdater;
        this.passwordChanger = passwordChanger;
    }

    public RegistrationResultDto register(RegisterUserDto userDto) {
        return registrationHandler.register(userDto);
    }

    public String login(LoginDto loginDto) {
        return loginHandler.login(loginDto);
    }

    public UserDto updateUserProfile(UpdateUserDto updateUserDto) {
        return profileUpdater.updateUserProfile(updateUserDto);
    }

    public boolean changeUserPassword(ChangePasswordDto changePasswordDto) {
        return passwordChanger.changeUserPassword(changePasswordDto);
    }
}