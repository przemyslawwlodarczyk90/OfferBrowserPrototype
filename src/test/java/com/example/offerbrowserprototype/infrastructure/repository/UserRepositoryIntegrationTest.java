//package com.example.offerbrowserprototype.infrastructure.repository;
//
//import com.example.offerbrowserprototype.domain.user.User;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.junit.jupiter.api.extension.ExtendWith;
//
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@ExtendWith(SpringExtension.class)
//@DataMongoTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
// class UserRepositoryIntegrationTest {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @BeforeEach
//    public void setUp() {
//        userRepository.deleteAll();
//    }
//
//
//    @Test
//     void shouldFindUserByUsername() {
//
//        User user = new User();
//        user.setUsername("marianpazdzioch");
//        user.setEmail("marian.pazdzioch@example.com");
//        user.setPassword("tajnehaslo123");
//        user.setActive(true);
//
//        userRepository.save(user);
//
//        Optional<User> foundUser = userRepository.findByUsername("marianpazdzioch");
//
//        assertThat(foundUser).isPresent();
//        assertThat(foundUser.get().getEmail()).isEqualTo("marian.pazdzioch@example.com");
//    }
//
//
//    @Test
//     void shouldFindUserByEmail() {
//
//        User user = new User();
//        user.setUsername("arnoldboczek");
//        user.setEmail("arnold.boczek@example.com");
//        user.setPassword("haslodokabanosa");
//        user.setActive(true);
//
//        userRepository.save(user);
//
//        Optional<User> foundUser = userRepository.findByEmail("arnold.boczek@example.com");
//
//        assertThat(foundUser).isPresent();
//        assertThat(foundUser.get().getUsername()).isEqualTo("arnoldboczek");
//    }
//
//    /**
//     * Test sprawdzający brak wyników wyszukiwania dla nieistniejącego użytkownika.
//     */
//    @Test
//    public void shouldNotFindNonExistentUser() {
//        // Próba znalezienia użytkownika, który nie istnieje
//        Optional<User> foundUser = userRepository.findByUsername("halinakiepska");
//
//        // Weryfikacja
//        assertThat(foundUser).isNotPresent();
//    }
//
//    /**
//     * Test sprawdzający zapis i aktywację użytkownika.
//     */
//    @Test
//    public void shouldSaveAndActivateUser() {
//        // Dane testowe
//        User user = new User();
//        user.setUsername("ferdynandkiepski");
//        user.setEmail("ferdynand.kiepski@example.com");
//        user.setPassword("pieniadzezalazlem");
//        user.setActive(false);
//
//        // Zapis użytkownika do bazy
//        User savedUser = userRepository.save(user);
//
//        // Aktualizacja statusu aktywności
//        savedUser.setActive(true);
//        userRepository.save(savedUser);
//
//        // Pobranie zaktualizowanego użytkownika
//        Optional<User> updatedUser = userRepository.findByUsername("ferdynandkiepski");
//
//        // Weryfikacja
//        assertThat(updatedUser).isPresent();
//        assertThat(updatedUser.get().isActive()).isTrue();
//    }
//}
