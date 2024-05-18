package com.offerbrowserprototype.domain.loginaandregister;

import com.offerbrowserprototype.domain.loginaandregister.dto.*;
import com.offerbrowserprototype.domain.user.UserDTO;
import com.offerbrowserprototype.infrastructure.security.jwt.JwtTokenUtil;
import org.springframework.stereotype.Component;

@Component
public class LoginAndRegisterFacade {

    private final UserRegistrationHandler registrationHandler;
    private final UserLoginHandler loginHandler;
    private final UserProfileUpdater profileUpdater;
    private final UserPasswordChanger passwordChanger;
    private final JwtTokenUtil jwtTokenUtil;

    public LoginAndRegisterFacade(UserRegistrationHandler registrationHandler,
                                  UserLoginHandler loginHandler,
                                  UserProfileUpdater profileUpdater,
                                  UserPasswordChanger passwordChanger,
                                  JwtTokenUtil jwtTokenUtil) {
        this.registrationHandler = registrationHandler;
        this.loginHandler = loginHandler;
        this.profileUpdater = profileUpdater;
        this.passwordChanger = passwordChanger;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public RegistrationResultDTO register(RegisterUserDTO userDto) {
        return registrationHandler.register(userDto);
    }

    public String login(LoginDTO loginDto) {
        String username = loginDto.getUsername();
        // Upewnij się, że sprawdzasz hasło przed generowaniem tokena
        return jwtTokenUtil.generateToken(username);
    }

    public UserDTO updateUserProfile(UpdateUserDTO updateUserDto) {
        return profileUpdater.updateUserProfile(updateUserDto);
    }

    public boolean changeUserPassword(ChangePasswordDTO changePasswordDto) {
        return passwordChanger.changeUserPassword(changePasswordDto);
    }
}
