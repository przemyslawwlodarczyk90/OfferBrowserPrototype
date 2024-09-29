package com.example.offerbrowserprototype.domain.loginaandregister;

import com.example.offerbrowserprototype.domain.loginaandregister.dto.RegisterUserDTO;
import com.example.offerbrowserprototype.domain.loginaandregister.dto.RegistrationResultDTO;
import com.example.offerbrowserprototype.domain.user.User;
import com.example.offerbrowserprototype.domain.user.UserMapper;
import com.example.offerbrowserprototype.domain.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
@Component
public class UserRegistrationHandler {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserRegistrationHandler(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    public RegistrationResultDTO register(RegisterUserDTO userDto) {
        // Sprawdzamy, czy użytkownik o podanym username już istnieje
        if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
            return new RegistrationResultDTO(null, userDto.getUsername(), false, "Username already taken");
        }

        // Hashowanie hasła
        String hashedPassword = passwordEncoder.encode(userDto.getPassword());

        // Tworzenie użytkownika za pomocą `UserMapper`
        User newUser = userMapper.toEntity(userDto, hashedPassword);

        // Zapis do bazy danych
        userRepository.save(newUser);

        return new RegistrationResultDTO(newUser.getId(), userDto.getUsername(), true, "Rejestracja udana");
    }
}
