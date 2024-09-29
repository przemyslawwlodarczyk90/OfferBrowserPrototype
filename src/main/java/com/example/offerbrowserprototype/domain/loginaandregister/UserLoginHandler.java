package com.example.offerbrowserprototype.domain.loginaandregister;

import com.example.offerbrowserprototype.domain.dto.loginandregister.LoginDto;

import com.example.offerbrowserprototype.infrastructure.repository.UserRepository;
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

    public String login(LoginDto loginDto) {
        // Wyszukujemy użytkownika po nazwie użytkownika
        return userRepository.findByUsername(loginDto.getUsername())
                .filter(user -> passwordEncoder.matches(loginDto.getPassword(), user.getPassword()))
                .map(user -> "token_jwt")  // Jeśli hasło jest poprawne, generujemy token (na razie placeholder)
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));
    }
}
