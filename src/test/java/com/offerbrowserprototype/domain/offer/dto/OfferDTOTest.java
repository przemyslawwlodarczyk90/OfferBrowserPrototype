package com.offerbrowserprototype.domain.offer.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

 class OfferDTOTest {

    @Test
     void testNoArgsConstructor() {
        OfferDTO offerDTO = new OfferDTO();
        assertNotNull(offerDTO);
    }

    @Test
     void testAllArgsConstructor() {
        Long id = 1L;
        String title = "Test Title";
        String description = "Test Description";
        String location = "Test Location";
        String salaryRange = "Test Salary Range";
        String technologies = "Test Technologies";

        OfferDTO offerDTO = new OfferDTO(id, title, description, location, salaryRange, technologies);

        assertEquals(id, offerDTO.getId());
        assertEquals(title, offerDTO.getTitle());
        assertEquals(description, offerDTO.getDescription());
        assertEquals(location, offerDTO.getLocation());
        assertEquals(salaryRange, offerDTO.getSalaryRange());
        assertEquals(technologies, offerDTO.getTechnologies());
    }

    @Test
     void testSettersAndGetters() {
        Long id = 1L;
        String title = "Test Title";
        String description = "Test Description";
        String location = "Test Location";
        String salaryRange = "Test Salary Range";
        String technologies = "Test Technologies";

        OfferDTO offerDTO = new OfferDTO();
        offerDTO.setId(id);
        offerDTO.setTitle(title);
        offerDTO.setDescription(description);
        offerDTO.setLocation(location);
        offerDTO.setSalaryRange(salaryRange);
        offerDTO.setTechnologies(technologies);

        assertEquals(id, offerDTO.getId());
        assertEquals(title, offerDTO.getTitle());
        assertEquals(description, offerDTO.getDescription());
        assertEquals(location, offerDTO.getLocation());
        assertEquals(salaryRange, offerDTO.getSalaryRange());
        assertEquals(technologies, offerDTO.getTechnologies());
    }

    @Test
     void testEqualsAndHashCode() {
        OfferDTO offerDTO1 = new OfferDTO(1L, "Test Title", "Test Description", "Test Location", "Test Salary Range", "Test Technologies");
        OfferDTO offerDTO2 = new OfferDTO(1L, "Test Title", "Test Description", "Test Location", "Test Salary Range", "Test Technologies");
        OfferDTO offerDTO3 = new OfferDTO(2L, "Another Title", "Another Description", "Another Location", "Another Salary Range", "Another Technologies");

        assertEquals(offerDTO1, offerDTO2);
        assertNotEquals(offerDTO1, offerDTO3);

        assertEquals(offerDTO1.hashCode(), offerDTO2.hashCode());
        assertNotEquals(offerDTO1.hashCode(), offerDTO3.hashCode());
    }

    @Test
     void testToString() {
        OfferDTO offerDTO = new OfferDTO(1L, "Test Title", "Test Description", "Test Location", "Test Salary Range", "Test Technologies");
        String expectedString = "OfferDTO(id=1, title=Test Title, description=Test Description, location=Test Location, salaryRange=Test Salary Range, technologies=Test Technologies)";

        assertEquals(expectedString, offerDTO.toString());
    }
}