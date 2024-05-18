package com.offerbrowserprototype.infrastracture.web;

import com.offerbrowserprototype.domain.loginaandregister.LoginAndRegisterFacade;
import com.offerbrowserprototype.domain.loginaandregister.dto.*;
import com.offerbrowserprototype.domain.user.UserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class LoginAndRegisterController {

    private final LoginAndRegisterFacade loginAndRegisterFacade;

    public LoginAndRegisterController(LoginAndRegisterFacade loginAndRegisterFacade) {
        this.loginAndRegisterFacade = loginAndRegisterFacade;
    }

    @PostMapping("/register")
    public ResponseEntity<RegistrationResultDTO> register(@RequestBody RegisterUserDTO registerUserDTO) {
        RegistrationResultDTO result = loginAndRegisterFacade.register(registerUserDTO);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO) {
        String token = loginAndRegisterFacade.login(loginDTO);
        return ResponseEntity.ok(token);
    }

    @PutMapping("/updateProfile")
    public ResponseEntity<UserDTO> updateUserProfile(@RequestBody UpdateUserDTO updateUserDTO) {
        UserDTO updatedUser = loginAndRegisterFacade.updateUserProfile(updateUserDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/changePassword")
    public ResponseEntity<Boolean> changeUserPassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
        boolean isChanged = loginAndRegisterFacade.changeUserPassword(changePasswordDTO);
        return ResponseEntity.ok(isChanged);
    }
}