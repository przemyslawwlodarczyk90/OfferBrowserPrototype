package com.offerbrowserprototype.domain.loginaandregister;

import com.offerbrowserprototype.domain.exception.UserNotFoundException;
import com.offerbrowserprototype.domain.loginaandregister.dto.UpdateUserDTO;
import com.offerbrowserprototype.domain.user.User;
import com.offerbrowserprototype.domain.user.UserDTO;
import com.offerbrowserprototype.infrastructure.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class UserProfileUpdater {

    @Autowired
    private UserRepository userRepository;

    UserDTO updateUserProfile(UpdateUserDTO updateUserDto) {
        User user = userRepository.findById(updateUserDto.getId()).orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setUsername(updateUserDto.getUsername());
        user.setEmail(updateUserDto.getEmail());
        userRepository.save(user);
        return new UserDTO(user.getId(), user.getUsername(), user.getEmail());
    }
}