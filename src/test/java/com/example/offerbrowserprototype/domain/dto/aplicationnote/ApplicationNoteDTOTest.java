//package com.example.offerbrowserprototype.domain.dto.aplicationnote;
//
//import org.junit.jupiter.api.Test;
//
//import java.time.LocalDateTime;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//class ApplicationNoteDTOTest {
//
//    @Test
//     void shouldCreateApplicationNoteDTOWithAllFields() {
//        // Given
//        String id = "123";
//        String offerId = "offer123";
//        String offerUrl = "https://kiepscy-jobs.com/offers/ferdynand";
//        String companyName = "Kiepscy Enterprises";
//        LocalDateTime appliedAt = LocalDateTime.now();
//
//        // When
//        ApplicationNoteDTO dto = new ApplicationNoteDTO();
//        dto.setId(id);
//        dto.setOfferId(offerId);
//        dto.setOfferUrl(offerUrl);
//        dto.setCompanyName(companyName);
//        dto.setAppliedAt(appliedAt);
//
//        // Then
//        assertThat(dto.getId()).isEqualTo(id);
//        assertThat(dto.getOfferId()).isEqualTo(offerId);
//        assertThat(dto.getOfferUrl()).isEqualTo(offerUrl);
//        assertThat(dto.getCompanyName()).isEqualTo(companyName);
//        assertThat(dto.getAppliedAt()).isEqualTo(appliedAt);
//    }
//
//    @Test
//     void shouldUpdateFieldsInApplicationNoteDTO() {
//        // Given
//        ApplicationNoteDTO dto = new ApplicationNoteDTO();
//
//        // When
//        dto.setId("123");
//        dto.setOfferId("offer456");
//        dto.setOfferUrl("https://kiepscy-jobs.com/offers/pazdzioch");
//        dto.setCompanyName("Paździoch Holdings");
//        dto.setAppliedAt(LocalDateTime.of(2025, 1, 7, 12, 0));
//// Then
//        assertThat(dto.getId()).isEqualTo("123");
//        assertThat(dto.getOfferId()).isEqualTo("offer456");
//        assertThat(dto.getOfferUrl()).isEqualTo("https://kiepscy-jobs.com/offers/pazdzioch");
//        assertThat(dto.getCompanyName()).isEqualTo("Paździoch Holdings");
//        assertThat(dto.getAppliedAt()).isEqualTo(LocalDateTime.of(2025, 1, 7, 12, 0));
//    }
//
//    @Test
//     void shouldSupportEqualityBasedOnAllFields() {
//        // Given
//        ApplicationNoteDTO dto1 = new ApplicationNoteDTO();
//        dto1.setId("123");
//        dto1.setOfferId("offer123");
//        dto1.setOfferUrl("https://kiepscy-jobs.com/offers/ferdynand");
//        dto1.setCompanyName("Kiepscy Enterprises");
//        dto1.setAppliedAt(LocalDateTime.of(2025, 1, 7, 12, 0));
//
//        ApplicationNoteDTO dto2 = new ApplicationNoteDTO();
//        dto2.setId("123");
//        dto2.setOfferId("offer123");
//        dto2.setOfferUrl("https://kiepscy-jobs.com/offers/ferdynand");
//        dto2.setCompanyName("Kiepscy Enterprises");
//        dto2.setAppliedAt(LocalDateTime.of(2025, 1, 7, 12, 0));
//
//        // Then
//        assertThat(dto1).isEqualTo(dto2);
//        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
//    }
//
//    @Test
//     void shouldNotBeEqualWhenFieldsDiffer() {
//        // Given
//        ApplicationNoteDTO dto1 = new ApplicationNoteDTO();
//        dto1.setId("123");
//        dto1.setOfferId("offer123");
//        dto1.setOfferUrl("https://kiepscy-jobs.com/offers/ferdynand");
//        dto1.setCompanyName("Kiepscy Enterprises");
//        dto1.setAppliedAt(LocalDateTime.of(2025, 1, 7, 12, 0));
//
//        ApplicationNoteDTO dto2 = new ApplicationNoteDTO();
//        dto2.setId("456");
//        dto2.setOfferId("offer456");
//        dto2.setOfferUrl("https://kiepscy-jobs.com/offers/pazdzioch");
//        dto2.setCompanyName("Paździoch Holdings");
//        dto2.setAppliedAt(LocalDateTime.of(2025, 1, 8, 12, 0));
//
//        // Then
//        assertThat(dto1).isNotEqualTo(dto2);
//    }
//
//    @Test
//     void shouldReturnCorrectStringRepresentation() {
//        // Given
//        ApplicationNoteDTO dto = new ApplicationNoteDTO();
//        dto.setId("123");
//        dto.setOfferId("offer123");
//        dto.setOfferUrl("https://kiepscy-jobs.com/offers/ferdynand");
//        dto.setCompanyName("Kiepscy Enterprises");
//        dto.setAppliedAt(LocalDateTime.of(2025, 1, 7, 12, 0));
//
//        // When
//        String result = dto.toString();
//
//        // Then
//        assertThat(result).contains("id=123");
//        assertThat(result).contains("offerId=offer123");
//        assertThat(result).contains("offerUrl=https://kiepscy-jobs.com/offers/ferdynand");
//        assertThat(result).contains("companyName=Kiepscy Enterprises");
//        assertThat(result).contains("appliedAt=2025-01-07T12:00");
//    }}