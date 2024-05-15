package com.offerbrowserprototype.domain.loginaandregister;

import com.offerbrowserprototype.domain.loginaandregister.dto.RegisterUserDto;
import com.offerbrowserprototype.domain.loginaandregister.dto.RegistrationResultDto;

class UserRegistrationHandler {

    RegistrationResultDto register(RegisterUserDto userDto) {
        return new RegistrationResultDto(userDto.getUsername(), true, "Rejestracja udana");
    }
}