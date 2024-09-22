package com.example.offerbrowserprototype.domain.loginaandregister;

import com.example.offerbrowserprototype.domain.loginaandregister.dto.UpdateUserDto;
import com.example.offerbrowserprototype.domain.user.UserDTO;
import org.springframework.stereotype.Component;

@Component
class UserProfileUpdater {

    public UserDTO updateUserProfile(UpdateUserDto updateUserDto) {
        return new UserDTO(updateUserDto.getId(), updateUserDto.getUsername(), updateUserDto.getEmail());
    }
}
