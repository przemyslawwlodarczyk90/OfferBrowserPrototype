package com.offerbrowserprototype.domain.loginaandregister;

import com.offerbrowserprototype.domain.loginaandregister.dto.UpdateUserDto;
import com.offerbrowserprototype.domain.user.UserDTO;
import org.springframework.stereotype.Component;

@Component
class UserProfileUpdater {

    public UserDTO updateUserProfile(UpdateUserDto updateUserDto) {
        return new UserDTO(updateUserDto.getId(), updateUserDto.getUsername(), updateUserDto.getEmail());
    }
}
