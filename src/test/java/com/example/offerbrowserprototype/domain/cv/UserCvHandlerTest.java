package com.example.offerbrowserprototype.domain.cv;

import com.example.offerbrowserprototype.domain.exception.NotFoundException;
import com.example.offerbrowserprototype.domain.user.Role;
import com.example.offerbrowserprototype.domain.user.User;
import com.example.offerbrowserprototype.infrastructure.repository.UserRepository;
import com.example.offerbrowserprototype.infrastructure.service.CvStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

// CV użytkownika: plik na dysku, w bazie tylko ścieżka (users.cv_url) — H2 w pamięci
@DataJpaTest
class UserCvHandlerTest {

    private static final byte[] PDF = "%PDF-1.4 test".getBytes(StandardCharsets.US_ASCII);

    @TempDir
    Path dir;

    @Autowired
    private UserRepository users;

    private UserCvHandler handler;

    @BeforeEach
    void setUp() {
        handler = new UserCvHandler(users, new CvStorageService(dir.toString(), 10));
        User u = new User();
        u.setUsername("ola");
        u.setEmail("ola@example.com");
        u.setPassword("x");
        u.setActive(true);
        u.setRole(Role.USER);
        users.save(u);
    }

    private void upload(String name) throws IOException {
        handler.upload("ola", name, PDF.length, new ByteArrayInputStream(PDF));
    }

    @Test
    void wgranie_zapisuje_sciezke_w_bazie() throws IOException {
        upload("CV Ola.pdf");

        String url = users.findByUsername("ola").orElseThrow().getCvUrl();
        assertThat(url).contains("cv_").endsWith("_CV Ola.pdf");
        assertThat(Files.exists(Path.of(url))).isTrue();
        assertThat(handler.info("ola").orElseThrow().fileName()).isEqualTo("CV Ola.pdf");
        assertThat(handler.file("ola")).isEqualTo(Path.of(url));
    }

    @Test
    void nowe_cv_zastepuje_stare_i_usuwa_poprzedni_plik() throws IOException {
        upload("stare.pdf");
        String old = users.findByUsername("ola").orElseThrow().getCvUrl();

        upload("nowe.pdf");

        String current = users.findByUsername("ola").orElseThrow().getCvUrl();
        assertThat(current).endsWith("_nowe.pdf");
        assertThat(Files.exists(Path.of(old))).isFalse();
        try (var files = Files.list(dir)) {
            assertThat(files).hasSize(1);
        }
    }

    @Test
    void odrzucony_plik_nie_zmienia_dotychczasowego_cv() throws IOException {
        upload("dobre.pdf");
        String before = users.findByUsername("ola").orElseThrow().getCvUrl();

        assertThatThrownBy(() -> handler.upload("ola", "zle.pdf", 3, new ByteArrayInputStream("abc".getBytes())))
                .isInstanceOf(IllegalArgumentException.class);

        assertThat(users.findByUsername("ola").orElseThrow().getCvUrl()).isEqualTo(before);
        assertThat(Files.exists(Path.of(before))).isTrue();
    }

    @Test
    void usuniecie_czysci_baze_i_plik() throws IOException {
        upload("cv.pdf");
        String url = users.findByUsername("ola").orElseThrow().getCvUrl();

        handler.delete("ola");

        assertThat(users.findByUsername("ola").orElseThrow().getCvUrl()).isNull();
        assertThat(Files.exists(Path.of(url))).isFalse();
        assertThat(handler.info("ola")).isEmpty();
        assertThatThrownBy(() -> handler.delete("ola")).isInstanceOf(NotFoundException.class);
        assertThatThrownBy(() -> handler.file("ola")).isInstanceOf(NotFoundException.class);
    }
}
