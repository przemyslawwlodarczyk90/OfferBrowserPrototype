package com.example.offerbrowserprototype.infrastructure.repository;

import com.example.offerbrowserprototype.domain.user.ConfirmationToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataMongoTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
 class ConfirmationTokenRepositoryIntegrationTest {

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @BeforeEach
    public void setUp() {
        confirmationTokenRepository.deleteAll();
    }


    @Test
     void shouldSaveAndRetrieveConfirmationTokenByToken() {

        ConfirmationToken token = new ConfirmationToken(
                "token123",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                "ferdynand"
        );

        confirmationTokenRepository.save(token);

        Optional<ConfirmationToken> retrievedToken = confirmationTokenRepository.findByToken("token123");

        assertThat(retrievedToken).isPresent();
        assertThat(retrievedToken.get().getUserId()).isEqualTo("ferdynand");
    }

    @Test
     void shouldNotFindNonExistentToken() {
        Optional<ConfirmationToken> retrievedToken = confirmationTokenRepository.findByToken("nieistniejacyToken");

        assertThat(retrievedToken).isNotPresent();
    }


    @Test
     void shouldDeleteConfirmationTokenByUserId() {
        ConfirmationToken token1 = new ConfirmationToken(
                "token456",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                "pazdzioch"
        );
        ConfirmationToken token2 = new ConfirmationToken(
                "token789",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(2),
                "pazdzioch"
        );

        confirmationTokenRepository.save(token1);
        confirmationTokenRepository.save(token2);

        confirmationTokenRepository.deleteByUserId("pazdzioch");

        assertThat(confirmationTokenRepository.findByToken("token456")).isNotPresent();
        assertThat(confirmationTokenRepository.findByToken("token789")).isNotPresent();
    }
}
