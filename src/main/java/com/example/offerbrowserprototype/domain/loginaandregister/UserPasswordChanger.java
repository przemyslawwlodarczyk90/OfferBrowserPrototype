package com.example.offerbrowserprototype.domain.loginaandregister;

import com.example.offerbrowserprototype.domain.dto.loginandregister.ChangePasswordDto;
import com.example.offerbrowserprototype.infrastructure.repository.UserRepository;
import com.example.offerbrowserprototype.domain.user.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
class UserPasswordChanger {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserPasswordChanger(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean changeUserPassword(ChangePasswordDto changePasswordDto) {
        // Wyszukaj użytkownika po nazwie użytkownika lub e-mailu (dostosuj do Twojej logiki)
        return userRepository.findByUsername(changePasswordDto.getUsername())
                .map(user -> {
                    // Sprawdź, czy obecne hasło jest poprawne
                    if (passwordEncoder.matches(changePasswordDto.getCurrentPassword(), user.getPassword())) {
                        // Ustaw nowe hasło i zapisz użytkownika
                        user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
                        userRepository.save(user);
                        return true;
                    } else {
                        // Jeśli hasło jest niepoprawne, zwróć `false`
                        return false;
                    }
                })
                .orElse(false); // Użytkownik nie znaleziony
    }
}
