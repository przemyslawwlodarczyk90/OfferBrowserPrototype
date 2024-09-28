package com.example.offerbrowserprototype.domain.user;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ConfirmationTokenDTO {
    private String id;
    private String token;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private LocalDateTime confirmedAt;
    private String userId; // Referencja do użytkownika

    public ConfirmationTokenDTO(String id, String token, LocalDateTime createdAt, LocalDateTime expiresAt, LocalDateTime confirmedAt, String userId) {
        this.id = id;
        this.token = token;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.confirmedAt = confirmedAt;
        this.userId = userId;
    }
}
