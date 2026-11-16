package com.example.offerbrowserprototype.infrastructure.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CvStorageServiceTest {

    private static final byte[] PDF = "%PDF-1.7\n1 0 obj\n<<>>\nendobj\n%%EOF".getBytes(StandardCharsets.US_ASCII);
    private static final byte[] DOCX = {'P', 'K', 3, 4, 20, 0, 6, 0, 8, 0};

    @TempDir
    Path dir;

    private CvStorageService storage(long maxMb) {
        return new CvStorageService(dir.toString(), maxMb);
    }

    private String store(CvStorageService s, String name, byte[] content) throws IOException {
        return s.store(7L, name, content.length, new ByteArrayInputStream(content));
    }

    @Test
    void zapisuje_plik_w_katalogu_z_properties_a_w_bazie_jest_tylko_sciezka() throws IOException {
        CvStorageService s = storage(10);

        String url = store(s, "Jan Kowalski – CV.pdf", PDF);

        assertThat(url).startsWith(dir.toString().replace('\\', '/') + "/cv_7_").endsWith("_Jan Kowalski – CV.pdf");
        assertThat(Files.readAllBytes(Path.of(url))).isEqualTo(PDF);
        CvStorageService.StoredCv cv = s.find(url).orElseThrow();
        assertThat(cv.fileName()).isEqualTo("Jan Kowalski – CV.pdf");
        assertThat(cv.size()).isEqualTo(PDF.length);
        assertThat(cv.contentType()).isEqualTo("application/pdf");
        try (var files = Files.list(dir)) {
            assertThat(files.map(p -> p.getFileName().toString())).noneMatch(n -> n.startsWith(".cv_upload_"));
        }
    }

    @Test
    void docx_jest_akceptowany_po_sygnaturze_zip() throws IOException {
        String url = store(storage(10), "cv.DOCX", DOCX);
        assertThat(storage(10).find(url).orElseThrow().contentType()).contains("wordprocessingml");
    }

    @Test
    void odrzuca_niedozwolony_format_i_falszywa_zawartosc() throws IOException {
        CvStorageService s = storage(10);
        assertThatThrownBy(() -> store(s, "cv.exe", PDF)).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("PDF");
        assertThatThrownBy(() -> store(s, "cv.pdf", "MZ to nie jest pdf".getBytes(StandardCharsets.US_ASCII)))
                .isInstanceOf(IllegalArgumentException.class).hasMessageContaining("nie pasuje");
        assertThatThrownBy(() -> store(s, "cv.pdf", new byte[0])).isInstanceOf(IllegalArgumentException.class);
        try (var files = Files.list(dir)) {
            assertThat(files).isEmpty();
        }
    }

    @Test
    void odrzuca_za_duzy_plik() {
        CvStorageService s = storage(1);
        byte[] big = new byte[1024 * 1024 + 1];
        System.arraycopy(PDF, 0, big, 0, PDF.length);
        assertThatThrownBy(() -> store(s, "cv.pdf", big)).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("1 MB");
    }

    @Test
    void nazwa_pliku_nie_wychodzi_poza_katalog() throws IOException {
        String url = store(storage(10), "..\\..\\windows/../etc/passwd.pdf", PDF);
        assertThat(Path.of(url).getParent().toAbsolutePath().normalize()).isEqualTo(dir.toAbsolutePath().normalize());
        assertThat(CvStorageService.sanitize("C:\\Users\\jan\\Moje CV?.pdf")).isEqualTo("Moje CV.pdf");
        assertThat(CvStorageService.sanitize(".pdf")).isEqualTo("CV");
        assertThat(CvStorageService.sanitize("a".repeat(300) + ".pdf")).hasSize(120).endsWith(".pdf");
    }

    @Test
    void usuniecie_i_brak_pliku() throws IOException {
        CvStorageService s = storage(10);
        String url = store(s, "cv.pdf", PDF);

        s.delete(url);

        assertThat(Files.exists(Path.of(url))).isFalse();
        assertThat(s.find(url)).isEmpty();
        assertThat(s.find(null)).isEmpty();
    }

    @Test
    void nazwa_wyswietlana_bez_prefiksu() {
        assertThat(CvStorageService.displayName("cv_5_20261022-143000_Moje CV.pdf")).isEqualTo("Moje CV.pdf");
        assertThat(CvStorageService.displayName("inny.pdf")).isEqualTo("inny.pdf");
    }
}
