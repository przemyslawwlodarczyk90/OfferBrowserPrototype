package com.offerbrowserprototype.domain.loginaandregister.dto;

import lombok.Data;

@Data
public class RegistrationResultDTO {
    private Long userId;
    private String username;
    private boolean isSuccess;
    private String message;
}