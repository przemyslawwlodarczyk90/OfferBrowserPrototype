package com.example.offerbrowserprototype.domain.user;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "confirmation_tokens",
        indexes = {
                @Index(name = "idx_token_unique", columnList = "token", unique = true)
        }
)
public class ConfirmationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Column(nullable = true)
    private LocalDateTime confirmedAt;


    @Column(nullable = false)
    private String userId;
}
