package com.offerbrowserprototype.domain.loginaandregister;

import com.offerbrowserprototype.domain.loginaandregister.dto.ChangePasswordDto;
import org.springframework.stereotype.Component;

@Component
class UserPasswordChanger {

    public boolean changeUserPassword(ChangePasswordDto changePasswordDto) {
        // Here you should implement actual password change logic
        return true;
    }
}
