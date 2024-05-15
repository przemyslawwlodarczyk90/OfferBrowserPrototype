package com.offerbrowserprototype.domain.loginaandregister;

import com.offerbrowserprototype.domain.loginaandregister.dto.LoginDto;

class UserLoginHandler {

    String login(LoginDto loginDto) {
        return "token_jwt";
    }
}