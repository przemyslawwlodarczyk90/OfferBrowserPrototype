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

    public LoginResult login(LoginDto loginDto) {
        return userRepository.findByUsername(loginDto.getUsername())
                .filter(user -> passwordEncoder.matches(loginDto.getPassword(), user.getPassword()))
                .map(user -> {
                    // WAŻNE: przekazujemy username jako subject JWT, NIE email
                    // CustomUserDetailsService.loadUserByUsername(username) musi działać
                    org.springframework.security.core.userdetails.User principal =
                            new org.springframework.security.core.userdetails.User(
                                    user.getUsername(),   // ← sub = username
                                    user.getPassword(),
                                    new ArrayList<>()
                            );

                    String token = jwtService.generateToken(principal);

                    return new LoginResult(token, user.getId(), user.getUsername(), user.getEmail());
                })
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));
    }
}