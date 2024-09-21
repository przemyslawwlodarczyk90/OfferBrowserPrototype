package com.offerbrowserprototype.domain.loginaandregister.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationResultDTO {
    private Long userId;
    private String username;
    private boolean isSuccess;
    private String message;

    public RegistrationResultDTO(String username, boolean isSuccess, String message) {
        this.username = username;
        this.isSuccess = isSuccess;
        this.message = message;
    }
}

