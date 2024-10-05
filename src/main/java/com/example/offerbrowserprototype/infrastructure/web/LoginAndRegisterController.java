package com.example.offerbrowserprototype.infrastructure.web;

import com.example.offerbrowserprototype.domain.dto.confirmationtoken.TokenResponse;
import com.example.offerbrowserprototype.domain.dto.loginandregister.*;
import com.example.offerbrowserprototype.domain.loginaandregister.LoginAndRegisterFacade;
import com.example.offerbrowserprototype.domain.dto.user.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Operations related to user authentication and profile management")
public class LoginAndRegisterController {

    private final LoginAndRegisterFacade loginAndRegisterFacade;

    public LoginAndRegisterController(LoginAndRegisterFacade loginAndRegisterFacade) {
        this.loginAndRegisterFacade = loginAndRegisterFacade;
    }

    @Operation(summary = "Register a new user", description = "Creates a new user account in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RegistrationResultDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data or username already exists", content = @Content)
    })
    @PostMapping("/register")
    public ResponseEntity<RegistrationResultDTO> register(@Valid @RequestBody RegisterUserDTO registerUserDTO) {
        RegistrationResultDTO result = loginAndRegisterFacade.register(registerUserDTO);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "User login", description = "Authenticates a user and returns a JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "401", description = "Invalid username or password", content = @Content)
    })
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginDto loginDto) {
        try {
            // Generowanie tokena JWT
            String token = loginAndRegisterFacade.login(loginDto);
            return new ResponseEntity<>(new TokenResponse(token), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new TokenResponse("Invalid username or password"), HttpStatus.UNAUTHORIZED);
        }
    }

    @Operation(summary = "Update user profile", description = "Updates the user profile information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User profile updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @PutMapping("/update")
    public ResponseEntity<UserDTO> updateProfile(@Valid @RequestBody UpdateUserDto updateUserDto) {
        try {
            UserDTO updatedUser = loginAndRegisterFacade.updateUserProfile(updateUserDto);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Change user password", description = "Changes the password of the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password changed successfully", content = @Content),
            @ApiResponse(responseCode = "400", description = "Failed to change password", content = @Content)
    })
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordDto changePasswordDto) {
        boolean isChanged = loginAndRegisterFacade.changeUserPassword(changePasswordDto);
        if (isChanged) {
            return new ResponseEntity<>("Password changed successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed to change password", HttpStatus.BAD_REQUEST);
        }
    }
}
