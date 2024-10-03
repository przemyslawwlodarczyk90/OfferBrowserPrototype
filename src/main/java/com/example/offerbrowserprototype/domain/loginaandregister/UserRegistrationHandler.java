package com.example.offerbrowserprototype.domain.loginaandregister;

import com.example.offerbrowserprototype.domain.dto.loginandregister.RegisterUserDTO;
import com.example.offerbrowserprototype.domain.dto.loginandregister.RegistrationResultDTO;
import com.example.offerbrowserprototype.domain.user.ConfirmationToken;
import com.example.offerbrowserprototype.domain.user.User;
import com.example.offerbrowserprototype.domain.mapper.UserMapper;

import com.example.offerbrowserprototype.infrastructure.repository.UserRepository;
import com.example.offerbrowserprototype.infrastructure.service.ConfirmationTokenService;
import com.example.offerbrowserprototype.infrastructure.service.MailService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;

@Component
class UserRegistrationHandler {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final MailService mailService;
    private final ConfirmationTokenService confirmationTokenService;
    private final Clock clock;

    public UserRegistrationHandler(UserRepository userRepository, PasswordEncoder passwordEncoder,
                                   UserMapper userMapper, MailService mailService,
                                   ConfirmationTokenService confirmationTokenService, Clock clock) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.mailService = mailService;
        this.confirmationTokenService = confirmationTokenService;
        this.clock = clock;
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

        // Generowanie linku potwierdzającego rejestrację
        String confirmationToken = generateConfirmationToken();
        String confirmationLink = "http://yourapp.com/api/v1/registration/confirm?token=" + confirmationToken;

        // Tworzenie tokenu rejestracyjnego i zapis do bazy danych
        ConfirmationToken token = new ConfirmationToken(
                confirmationToken,
                LocalDateTime.now(clock), // Użycie Clock do uzyskania aktualnego czasu
                LocalDateTime.now(clock).plusDays(1), // Token ważny przez 24 godziny
                newUser.getId()
        );
        confirmationTokenService.saveConfirmationToken(token);

        // Wysłanie e-maila potwierdzającego
        mailService.sendConfirmationEmail(userDto.getEmail(), "Confirm your registration", userDto.getUsername(), "", confirmationLink);

        return new RegistrationResultDTO(newUser.getId(), userDto.getUsername(), true, "Rejestracja udana");
    }

    private String generateConfirmationToken() {
        // Wygeneruj token potwierdzający (np. UUID lub token JWT)
        return java.util.UUID.randomUUID().toString();
    }
}
