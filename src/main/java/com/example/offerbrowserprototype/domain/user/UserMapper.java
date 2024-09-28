package com.example.offerbrowserprototype.domain.user;

import com.example.offerbrowserprototype.domain.loginaandregister.dto.RegisterUserDTO;
import com.example.offerbrowserprototype.domain.loginaandregister.dto.UpdateUserDto;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class UserMapper {

    // Konwersja z `User` na `UserDTO`
    public UserDTO toDTO(User user) {
        return new UserDTO(
                user.getId(), // Zachowuje `String` jako typ dla spójności z `User`
                user.getUsername(),
                user.getEmail()
        );
    }

    // Konwersja z `RegisterUserDTO` na `User`
    public User toEntity(RegisterUserDTO registerUserDTO, String hashedPassword) {
        User user = new User();
        user.setUsername(registerUserDTO.getUsername());
        user.setEmail(registerUserDTO.getEmail());
        user.setPassword(hashedPassword);
        return user;
    }

    // Aktualizacja istniejącego `User` z `UpdateUserDto`
    public void updateUserFromDto(UpdateUserDto updateUserDto, User user) {
        // Sprawdź, czy `username` jest nie `null` i nie jest pusty, zanim zaktualizujesz
        if (StringUtils.hasText(updateUserDto.getUsername())) {
            user.setUsername(updateUserDto.getUsername());
        }

        // Sprawdź, czy `email` jest nie `null` i nie jest pusty, zanim zaktualizujesz
        if (StringUtils.hasText(updateUserDto.getEmail())) {
            user.setEmail(updateUserDto.getEmail());
        }

    }
}
