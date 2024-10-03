package com.example.offerbrowserprototype.infrastructure.web;

import com.example.offerbrowserprototype.domain.loginaandregister.LoginAndRegisterFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/registration")
@Tag(name = "Registration", description = "Endpoints for user registration and confirmation")
@Validated // Dodanie adnotacji @Validated dla aktywacji walidacji w kontrolerze
public class RegistrationController {

    private final LoginAndRegisterFacade loginAndRegisterFacade;

    public RegistrationController(LoginAndRegisterFacade loginAndRegisterFacade) {
        this.loginAndRegisterFacade = loginAndRegisterFacade;
    }

    @Operation(summary = "Confirm user registration",
            description = "Confirms the registration of a user by validating the provided confirmation token. " +
                    "If the token is valid, the user account is activated.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registration confirmed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid or expired token")
    })
    @GetMapping("/confirm")
    public ResponseEntity<String> confirmRegistration(@RequestParam("token") @NotBlank String token) {
        try {
            loginAndRegisterFacade.confirmRegistration(token);
            return ResponseEntity.ok("Registration confirmed!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
