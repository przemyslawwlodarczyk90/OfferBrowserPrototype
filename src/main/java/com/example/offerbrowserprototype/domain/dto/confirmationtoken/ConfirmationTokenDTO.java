package com.example.offerbrowserprototype.domain.dto.confirmationtoken;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
public class ConfirmationTokenDTO {

    @NotNull
    private String id;

    @NotNull
    private String token;

    @NotNull
    private LocalDateTime createdAt;

    @NotNull
    private LocalDateTime expiresAt;

    private LocalDateTime confirmedAt; // Może być opcjonalne, więc bez walidacji

    @NotNull
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
