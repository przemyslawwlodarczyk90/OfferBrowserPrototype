package com.offerbrowserprototype.domain.loginaandregister;

import com.offerbrowserprototype.domain.user.UserDto;

class UserProfileUpdater {

    UserDto updateUserProfile(UpdateUserDto updateUserDto) {

        return new UserDto(updateUserDto.getId(), updateUserDto.getUsername(), "email@updated.com");
    }
}