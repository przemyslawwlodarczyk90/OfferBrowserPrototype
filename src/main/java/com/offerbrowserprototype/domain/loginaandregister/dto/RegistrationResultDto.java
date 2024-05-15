package com.offerbrowserprototype.domain.loginaandregister.dto;

import lombok.Data;

@Data
public class RegistrationResultDto {
    private Long userId;
    private String username;
    private boolean isSuccess;
    private String message;
}