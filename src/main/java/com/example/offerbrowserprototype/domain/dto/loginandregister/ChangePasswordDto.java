package com.example.offerbrowserprototype.domain.dto.loginandregister;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePasswordDto {

    @NotBlank
    private String id; // Zmieniono z `Long` na `String`

    @NotBlank
    @Size(min = 6, message = "Old password should have at least 6 characters")
    private String oldPassword;

    @NotBlank
    @Size(min = 6, message = "New password should have at least 6 characters")
    private String newPassword;
}
