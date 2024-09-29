package com.example.offerbrowserprototype.infrastructure.service;

import com.example.offerbrowserprototype.domain.user.ConfirmationToken;
import com.example.offerbrowserprototype.infrastructure.repository.ConfirmationTokenRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository tokenRepository;

    public ConfirmationTokenService(ConfirmationTokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public void saveConfirmationToken(ConfirmationToken token) {
        tokenRepository.save(token);
    }

    public Optional<ConfirmationToken> getToken(String token) {
        return tokenRepository.findByToken(token);
    }

    public void confirmToken(ConfirmationToken token) {
        // Sprawdź, czy token nie został już potwierdzony lub wygasł
        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Token has expired");
        }

        // Ustawienie tokenu jako potwierdzonego
        token.setConfirmedAt(LocalDateTime.now());
        tokenRepository.save(token);
    }
}