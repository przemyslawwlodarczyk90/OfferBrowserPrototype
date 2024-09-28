package com.example.offerbrowserprototype.domain.user;

import org.springframework.stereotype.Component;

@Component
public class ConfirmationTokenMapper {

    // Metoda do konwersji z `ConfirmationToken` na `ConfirmationTokenDTO`
    public ConfirmationTokenDTO toDTO(ConfirmationToken token) {
        return new ConfirmationTokenDTO(
                token.getId(),
                token.getToken(),
                token.getCreatedAt(),
                token.getExpiresAt(),
                token.getConfirmedAt(),
                token.getUserId()
        );
    }

    // Metoda do konwersji z `ConfirmationTokenDTO` na `ConfirmationToken`
    public ConfirmationToken toEntity(ConfirmationTokenDTO tokenDTO) {
        ConfirmationToken token = new ConfirmationToken();
        token.setId(tokenDTO.getId());
        token.setToken(tokenDTO.getToken());
        token.setCreatedAt(tokenDTO.getCreatedAt());
        token.setExpiresAt(tokenDTO.getExpiresAt());
        token.setConfirmedAt(tokenDTO.getConfirmedAt());
        token.setUserId(tokenDTO.getUserId());
        return token;
    }
}
