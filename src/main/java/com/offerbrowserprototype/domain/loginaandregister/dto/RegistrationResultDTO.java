package com.offerbrowserprototype.domain.loginaandregister.dto;

import lombok.Data;

@Data
public class RegistrationResultDTO {
    private Long userId;
    private String username;
    private boolean isSuccess;
    private String message;

    public RegistrationResultDTO(Long userId, String username, boolean isSuccess, String message) {
        this.userId = userId;
        this.username = username;
        this.isSuccess = isSuccess;
        this.message = message;
    }
}
