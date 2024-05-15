package com.offerbrowserprototype.application.facades;

import com.offerbrowserprototype.application.services.LoginAndRegisterService;
import com.offerbrowserprototype.domain.loginaandregister.dto.*;
import com.offerbrowserprototype.domain.user.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoginAndRegisterFacade {
    private final LoginAndRegisterService service;

    @Autowired
    public LoginAndRegisterFacade(LoginAndRegisterService service) {
        this.service = service;
    }

    public RegistrationResultDto register(RegisterUserDto userDto) {
        return service.register(userDto);
    }

    public String login(LoginDto loginDto) {
        return service.login(loginDto);
    }

    public UserDto updateUserProfile(UpdateUserDto updateUserDto) {
        return service.updateUserProfile(updateUserDto);
    }

    public boolean changeUserPassword(ChangePasswordDto changePasswordDto) {
        return service.changeUserPassword(changePasswordDto);
    }
}