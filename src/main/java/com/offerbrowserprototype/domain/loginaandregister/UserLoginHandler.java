package com.offerbrowserprototype.domain.loginaandregister;

import com.offerbrowserprototype.domain.exception.AuthorizationException;
import com.offerbrowserprototype.domain.loginaandregister.dto.LoginDTO;
import com.offerbrowserprototype.domain.user.User;
import com.offerbrowserprototype.infrastructure.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
class UserLoginHandler {


    private final UserRepository userRepository;


    private final PasswordEncoder passwordEncoder;

    public UserLoginHandler(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    String login(LoginDTO loginDto) {
        User user = userRepository.findByUsername(loginDto.getUsername());
        if (user == null || !passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new AuthorizationException("Invalid username or password");
        }
        return "token_jwt";
    }
}