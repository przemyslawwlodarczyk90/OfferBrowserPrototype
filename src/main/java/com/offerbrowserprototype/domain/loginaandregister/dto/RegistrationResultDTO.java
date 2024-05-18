package com.offerbrowserprototype.domain.loginaandregister.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RegistrationResultDTO {
    private Long userId;
    private String username;
    private boolean isSuccess;
    private String message;


}
