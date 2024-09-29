package com.example.offerbrowserprototype.domain.dto.loginandregister;

import lombok.Data;

@Data
public class ChangePasswordDto {
    private String id; // Zmieniono z `Long` na `String`
    private String oldPassword;
    private String newPassword;
}
