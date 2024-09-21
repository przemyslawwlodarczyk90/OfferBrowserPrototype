package com.offerbrowserprototype.domain.loginaandregister;


import com.offerbrowserprototype.domain.loginaandregister.dto.RegisterUserDTO;
import com.offerbrowserprototype.domain.loginaandregister.dto.RegistrationResultDTO;
import com.offerbrowserprototype.domain.loginaandregister.dto.UpdateUserDto;
import org.springframework.stereotype.Component;

@Component
public class UserRegistrationHandler {

    public RegistrationResultDTO register(RegisterUserDTO userDto) {
        return new RegistrationResultDTO(userDto.getUsername(), true, "Rejestracja udana");
    }
}
