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
public class UserRegistrationHandler {

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

        if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
            return new RegistrationResultDTO(null, userDto.getUsername(), false, "Username already taken");
        }


        String hashedPassword = passwordEncoder.encode(userDto.getPassword());


        User newUser = userMapper.toEntity(userDto, hashedPassword);


        userRepository.save(newUser);


        String confirmationToken = generateConfirmationToken();
        String confirmationLink = "http://localhost:8080/api/v1/registration/confirm?token=" + confirmationToken;


        ConfirmationToken token = new ConfirmationToken(
                confirmationToken,
                LocalDateTime.now(clock),
                LocalDateTime.now(clock).plusDays(1),
                newUser.getId()
        );
        confirmationTokenService.saveConfirmationToken(token);


        mailService.sendConfirmationEmail(userDto.getEmail(), "Confirm your registration", userDto.getUsername(), confirmationLink);

        return new RegistrationResultDTO(newUser.getId(), userDto.getUsername(), true, "Rejestracja udana");
    }

    private String generateConfirmationToken() {

        return java.util.UUID.randomUUID().toString();
    }
}
