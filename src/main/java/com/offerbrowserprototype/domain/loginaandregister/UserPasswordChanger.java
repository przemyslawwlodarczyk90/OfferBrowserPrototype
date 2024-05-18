package com.offerbrowserprototype.domain.loginaandregister;

import com.offerbrowserprototype.domain.exception.AuthorizationException;
import com.offerbrowserprototype.domain.exception.UserNotFoundException;
import com.offerbrowserprototype.domain.loginaandregister.dto.ChangePasswordDTO;
import com.offerbrowserprototype.domain.user.User;
import com.offerbrowserprototype.infrastructure.repository.UserRepository;
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

    boolean changeUserPassword(ChangePasswordDTO changePasswordDto) {
        User user = userRepository.findById(changePasswordDto.getId()).orElseThrow(() -> new UserNotFoundException("User not found"));
        if (!passwordEncoder.matches(changePasswordDto.getOldPassword(), user.getPassword())) {
            throw new AuthorizationException("Old password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
        userRepository.save(user);
        return true;
    }
}