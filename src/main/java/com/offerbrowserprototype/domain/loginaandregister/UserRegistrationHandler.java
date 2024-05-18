package com.offerbrowserprototype.domain.loginaandregister;

import com.offerbrowserprototype.domain.exception.InvalidRegisterException;
import com.offerbrowserprototype.domain.loginaandregister.dto.RegisterUserDTO;
import com.offerbrowserprototype.domain.loginaandregister.dto.RegistrationResultDTO;
import com.offerbrowserprototype.domain.user.User;
import com.offerbrowserprototype.infrastructure.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
class UserRegistrationHandler {


    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserRegistrationHandler(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    RegistrationResultDTO register(RegisterUserDTO userDto) {
        if (userRepository.findByUsername(userDto.getUsername()) != null) {
            throw new InvalidRegisterException("Username already exists");
        }

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        User savedUser = userRepository.save(user);

        return new RegistrationResultDTO(savedUser.getId(), savedUser.getUsername(), true, "Registration successful");
    }
}