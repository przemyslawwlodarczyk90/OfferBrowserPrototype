package com.example.offerbrowserprototype.domain.loginaandregister;

import com.example.offerbrowserprototype.domain.dto.loginandregister.UpdateUserDto;
import com.example.offerbrowserprototype.domain.user.User;
import com.example.offerbrowserprototype.domain.dto.user.UserDTO;
import com.example.offerbrowserprototype.domain.mapper.UserMapper;

import com.example.offerbrowserprototype.infrastructure.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
class UserProfileUpdater {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserProfileUpdater(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserDTO updateUserProfile(UpdateUserDto updateUserDto) {
        // Znajdź użytkownika w bazie danych
        User existingUser = userRepository.findById(updateUserDto.getId().toString())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Zaktualizuj dane użytkownika za pomocą `UserMapper`
        userMapper.updateUserFromDto(updateUserDto, existingUser);

        // Zapisz zmiany w bazie danych
        User updatedUser = userRepository.save(existingUser);

        return userMapper.toDTO(updatedUser);
    }
}
