package com.example.offerbrowserprototype.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String id; // Zmieniono z `Long` na `String` dla spójności z `User`
    private String username;
    private String email;
}
