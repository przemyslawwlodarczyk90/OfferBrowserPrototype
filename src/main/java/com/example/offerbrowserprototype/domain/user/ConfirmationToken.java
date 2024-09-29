package com.example.offerbrowserprototype.domain.user;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Document(collection = "confirmation_tokens")
public class ConfirmationToken {

    @Id
    private String id;
    private String token;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private LocalDateTime confirmedAt; // Pole wskazujące datę potwierdzenia tokenu
    private String userId; // Referencja do użytkownika

    public ConfirmationToken(String token, LocalDateTime createdAt, LocalDateTime expiresAt, String userId) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.userId = userId;
    }
}
