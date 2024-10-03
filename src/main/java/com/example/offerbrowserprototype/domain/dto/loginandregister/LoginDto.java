package com.example.offerbrowserprototype.domain.dto.loginandregister;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;



@Data
public class LoginDto {

    @NotBlank(message = "Username cannot be empty")
    private String username;

    @NotBlank(message = "Password cannot be empty")
    private String password;
}
