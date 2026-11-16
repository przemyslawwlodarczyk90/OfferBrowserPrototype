package com.example.offerbrowserprototype.domain.offer;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OfferSourceTest {

    @Test
    void rozpoznaje_parametr_api() {
        assertThat(OfferSource.fromParam("NoFluff")).contains(OfferSource.NOFLUFF);
        assertThat(OfferSource.fromParam("theprotocol")).contains(OfferSource.THEPROTOCOL);
        assertThat(OfferSource.fromParam(null)).isEmpty();
        assertThat(OfferSource.fromParam(" ")).isEmpty();
        assertThat(OfferSource.fromParam("JustJoin")).contains(OfferSource.JUSTJOIN);
        assertThatThrownBy(() -> OfferSource.fromParam("Pracuj")).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void wybiera_portal_po_domenie() {
        assertThat(OfferSource.fromUrl("https://nofluffjobs.com/pl/job/java-dev")).contains(OfferSource.NOFLUFF);
        assertThat(OfferSource.fromUrl("https://theprotocol.it/szczegoly/praca/x,oferta,1")).contains(OfferSource.THEPROTOCOL);
        assertThat(OfferSource.fromUrl("https://www.theprotocol.it/szczegoly/praca/x,oferta,1")).contains(OfferSource.THEPROTOCOL);
        assertThat(OfferSource.fromUrl("https://justjoin.it/job-offer/firma-java-developer-warszawa-java")).contains(OfferSource.JUSTJOIN);
        assertThat(OfferSource.JUSTJOIN.domain()).isEqualTo("justjoin.it");
        assertThat(OfferSource.supportedDomains()).isEqualTo("nofluffjobs.com, theprotocol.it, justjoin.it");
    }

    @Test
    void odrzuca_nieznane_i_podszywajace_sie_domeny() {
        assertThat(OfferSource.fromUrl("https://theprotocol.it.evil.com/x")).isEmpty();
        assertThat(OfferSource.fromUrl("https://eviltheprotocol.it/x")).isEmpty();
        assertThat(OfferSource.fromUrl("https://justjoin.it.evil.com/job-offer/x")).isEmpty();
        assertThat(OfferSource.fromUrl("https://pracuj.pl/praca/x")).isEmpty();
        assertThat(OfferSource.fromUrl("file:///etc/passwd")).isEmpty();
        assertThat(OfferSource.fromUrl("nie url")).isEmpty();
        assertThat(OfferSource.THEPROTOCOL.matchesUrl("https://nofluffjobs.com/pl/job/x")).isFalse();
    }
}
