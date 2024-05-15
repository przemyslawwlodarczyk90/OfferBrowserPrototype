package com.offerbrowserprototype.domain.loginaandregister;

import com.offerbrowserprototype.domain.user.UserDTO;

class UserProfileUpdater {

    UserDTO updateUserProfile(UpdateUserDto updateUserDto) {

        return new UserDTO(updateUserDto.getId(), updateUserDto.getUsername(), "email@updated.com");
    }
}