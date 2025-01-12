//package com.example.offerbrowserprototype.domain.dto.offer;
//
//import org.junit.jupiter.api.Test;
//
//import java.time.LocalDateTime;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//class OfferDTOTest {
//
//    @Test
//     void shouldCreateDefaultConstructor() {
//        // Given & When
//        OfferDTO offerDTO = new OfferDTO();
//
//        // Then
//        assertThat(offerDTO).isNotNull();
//        assertThat(offerDTO.getId()).isNull();
//        assertThat(offerDTO.getTitle()).isNull();
//        assertThat(offerDTO.getDescription()).isNull();
//        assertThat(offerDTO.getLocation()).isNull();
//        assertThat(offerDTO.getOfferUrl()).isNull();
//        assertThat(offerDTO.getSalaryRange()).isNull();
//        assertThat(offerDTO.getCompany()).isNull();
//        assertThat(offerDTO.getLevel()).isNull();
//        assertThat(offerDTO.isApplied()).isFalse();
//        assertThat(offerDTO.getFetchedAt()).isNull();
//    }
//
//    @Test
//     void shouldSetAndGetAllFields() {
//        // Given
//        OfferDTO offerDTO = new OfferDTO();
//        LocalDateTime fetchedAt = LocalDateTime.of(2025, 1, 7, 15, 0);
//
//        // When
//        offerDTO.setId("123");
//        offerDTO.setTitle("Software Engineer");
//        offerDTO.setDescription("Development of scalable applications");
//        offerDTO.setLocation("Remote");
//        offerDTO.setOfferUrl("https://example.com/offers/123");
//        offerDTO.setSalaryRange("10,000-15,000 USD");
//        offerDTO.setCompany("Example Corp");
//        offerDTO.setLevel("Mid");
//        offerDTO.setApplied(true);
//        offerDTO.setFetchedAt(fetchedAt);
//
//        // Then
//        assertThat(offerDTO.getId()).isEqualTo("123");
//        assertThat(offerDTO.getTitle()).isEqualTo("Software Engineer");
//        assertThat(offerDTO.getDescription()).isEqualTo("Development of scalable applications");
//        assertThat(offerDTO.getLocation()).isEqualTo("Remote");
//        assertThat(offerDTO.getOfferUrl()).isEqualTo("https://example.com/offers/123");
//        assertThat(offerDTO.getSalaryRange()).isEqualTo("10,000-15,000 USD");
//        assertThat(offerDTO.getCompany()).isEqualTo("Example Corp");
//        assertThat(offerDTO.getLevel()).isEqualTo("Mid");
//        assertThat(offerDTO.isApplied()).isTrue();
//        assertThat(offerDTO.getFetchedAt()).isEqualTo(fetchedAt);
//    }
//
//    @Test
//     void shouldTestParameterizedConstructor() {
//
//        LocalDateTime fetchedAt = LocalDateTime.of(2025, 1, 7, 15, 0);
//
//        OfferDTO offerDTO = new OfferDTO(
//                "123",
//                "Software Engineer",
//                "Development of scalable applications",
//                "Remote",
//                "https://example.com/offers/123",
//                "10,000-15,000 USD",
//                "Example Corp",
//                "Mid",
//                true,
//                fetchedAt
//        );
//
//        // Then
//        assertThat(offerDTO.getId()).isEqualTo("123");
//        assertThat(offerDTO.getTitle()).isEqualTo("Software Engineer");
//        assertThat(offerDTO.getDescription()).isEqualTo("Development of scalable applications");
//        assertThat(offerDTO.getLocation()).isEqualTo("Remote");
//        assertThat(offerDTO.getOfferUrl()).isEqualTo("https://example.com/offers/123");
//        assertThat(offerDTO.getSalaryRange()).isEqualTo("10,000-15,000 USD");
//        assertThat(offerDTO.getCompany()).isEqualTo("Example Corp");
//        assertThat(offerDTO.getLevel()).isEqualTo("Mid");
//        assertThat(offerDTO.isApplied()).isTrue();
//        assertThat(offerDTO.getFetchedAt()).isEqualTo(fetchedAt);
//    }
//
//    @Test
//     void shouldTestEqualsAndHashCode() {
//        LocalDateTime fetchedAt = LocalDateTime.of(2025, 1, 7, 15, 0);
//        OfferDTO dto1 = new OfferDTO(
//                "123",
//                "Software Engineer",
//                "Development of scalable applications",
//                "Remote",
//                "https://example.com/offers/123",
//                "10,000-15,000 USD",
//                "Example Corp",
//                "Mid",
//                true,
//                fetchedAt
//        );
//
//        OfferDTO dto2 = new OfferDTO(
//                "123",
//                "Software Engineer",
//                "Development of scalable applications",
//                "Remote",
//                "https://example.com/offers/123",
//                "10,000-15,000 USD",
//                "Example Corp",
//                "Mid",
//                true,
//                fetchedAt
//        );
//
//        assertThat(dto1).isEqualTo(dto2);
//        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
//    }
//
//    @Test
//     void shouldTestNotEquals() {
//        OfferDTO dto1 = new OfferDTO("Software Engineer", "Description 1", "Remote", "https://url1.com", "10,000-15,000 USD", "Company A", "Mid", false, LocalDateTime.now());
//        OfferDTO dto2 = new OfferDTO("Product Manager", "Description 2", "On-site", "https://url2.com", "15,000-20,000 USD", "Company B", "Senior", true, LocalDateTime.now());
//
//        assertThat(dto1).isNotEqualTo(dto2);
//    }
//
//}
