package com.offerbrowserprototype.domain.loginaandregister.dto;

import lombok.Data;

@Data
public class UpdateUserDTO {
    private Long id;
    private String username;
    private String email;
}