package com.example.offerbrowserprototype.domain.offer;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OfferValidationTest {

    private static final String URL = "https://nofluffjobs.com/pl/job/java-developer-acme-warszawa-1";

    @Test
    void poprawna_oferta_przechodzi() {
        assertThat(OfferValidation.rejectionReason(URL, "Java Developer", "ACME", OfferSource.NOFLUFF)).isNull();
    }

    @Test
    void odrzuca_brak_lub_teksty_zastepcze_parsera() {
        assertThat(OfferValidation.rejectionReason(URL, "Brak tytułu", "ACME", OfferSource.NOFLUFF)).contains("tytułu");
        assertThat(OfferValidation.rejectionReason(URL, " ", "ACME", OfferSource.NOFLUFF)).contains("tytułu");
        assertThat(OfferValidation.rejectionReason(URL, "Java", "Brak informacji o firmie", OfferSource.NOFLUFF)).contains("firmy");
        assertThat(OfferValidation.rejectionReason(URL, "Java", null, OfferSource.NOFLUFF)).contains("firmy");
        assertThat(OfferValidation.rejectionReason(null, "Java", "ACME", OfferSource.NOFLUFF)).contains("offerUrl");
        assertThat(OfferValidation.rejectionReason("https://example.com/x", "Java", "ACME", OfferSource.NOFLUFF)).contains("domeny");
    }
}
