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
public class ConfirmationTokenRepositoryIntegrationTest {

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @BeforeEach
    public void setUp() {
        confirmationTokenRepository.deleteAll(); // Czyszczenie bazy przed każdym testem
    }

    /**
     * Test sprawdzający zapis i odczyt tokena potwierdzającego z bazy.
     */
    @Test
    public void shouldSaveAndRetrieveConfirmationTokenByToken() {
        // Dane testowe: Ferdynand Kiepski
        ConfirmationToken token = new ConfirmationToken(
                "token123",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                "ferdynand"
        );

        // Zapis tokena do bazy
        confirmationTokenRepository.save(token);

        // Pobranie tokena z bazy
        Optional<ConfirmationToken> retrievedToken = confirmationTokenRepository.findByToken("token123");

        // Weryfikacja
        assertThat(retrievedToken).isPresent();
        assertThat(retrievedToken.get().getUserId()).isEqualTo("ferdynand");
    }

    /**
     * Test sprawdzający brak tokena w bazie.
     */
    @Test
    public void shouldNotFindNonExistentToken() {
        // Próba wyszukania tokena, który nie istnieje
        Optional<ConfirmationToken> retrievedToken = confirmationTokenRepository.findByToken("nieistniejacyToken");

        // Weryfikacja, że token nie istnieje
        assertThat(retrievedToken).isNotPresent();
    }

    /**
     * Test sprawdzający usunięcie tokenów użytkownika.
     */
    @Test
    public void shouldDeleteConfirmationTokenByUserId() {
        // Dane testowe: Marian Paździoch
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

        // Zapis tokenów do bazy
        confirmationTokenRepository.save(token1);
        confirmationTokenRepository.save(token2);

        // Usunięcie tokenów użytkownika "pazdzioch"
        confirmationTokenRepository.deleteByUserId("pazdzioch");

        // Weryfikacja, że tokeny zostały usunięte
        assertThat(confirmationTokenRepository.findByToken("token456")).isNotPresent();
        assertThat(confirmationTokenRepository.findByToken("token789")).isNotPresent();
    }
}
