package com.offerbrowserprototype.domain.loginaandregister;

import com.offerbrowserprototype.domain.loginaandregister.dto.LoginDto;
import org.springframework.stereotype.Component;

@Component
class UserLoginHandler {

    public String login(LoginDto loginDto) {
        // Return JWT token or any token logic
        return "token_jwt";
    }
}
