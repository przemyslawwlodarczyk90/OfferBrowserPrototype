package com.example.offerbrowserprototype.domain.loginaandregister;
import com.example.offerbrowserprototype.domain.dto.loginandregister.LoginDto;
import com.example.offerbrowserprototype.infrastructure.repository.UserRepository;
import com.example.offerbrowserprototype.infrastructure.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class UserLoginHandler {

    private final UserRepository  userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService      jwtService;

    public UserLoginHandler(UserRepository userRepository,
                            PasswordEncoder passwordEncoder,
                            JwtService jwtService) {
        this.userRepository  = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService      = jwtService;
    }

    // ── Klasa wynikowa — niesie token + dane użytkownika ─────────────────
    public static class LoginResult {
        public final String token;
        public final Long   userId;
        public final String username;
        public final String email;

        public LoginResult(String token, Long userId, String username, String email) {
            this.token    = token;
            this.userId   = userId;
            this.username = username;
            this.email    = email;
        }
    }

    // ── Główna metoda logowania ───────────────────────────────────────────
    public LoginResult login(LoginDto loginDto) {
        return userRepository.findByUsername(loginDto.getUsername())
                .filter(user -> passwordEncoder.matches(loginDto.getPassword(), user.getPassword()))
                .map(user -> {
                    String token = jwtService.generateToken(
                            new org.springframework.security.core.userdetails.User(
                                    user.getUsername(),
                                    user.getPassword(),
                                    new ArrayList<>()
                            )
                    );
                    return new LoginResult(token, user.getId(), user.getUsername(), user.getEmail());
                })
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));
    }
}


















