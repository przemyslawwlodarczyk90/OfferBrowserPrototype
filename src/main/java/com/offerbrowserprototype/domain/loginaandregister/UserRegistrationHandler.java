package com.offerbrowserprototype.domain.loginaandregister;

import com.offerbrowserprototype.domain.loginaandregister.dto.RegisterUserDTO;
import com.offerbrowserprototype.domain.loginaandregister.dto.RegistrationResultDTO;

class UserRegistrationHandler {

    RegistrationResultDTO register(RegisterUserDTO userDto) {
        return new RegistrationResultDTO(userDto.getUsername(), true, "Rejestracja udana");
    }
}