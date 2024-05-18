package com.offerbrowserprototype.domain.offer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OfferTest {

    @Test
    public void testNoArgsConstructor() {
        Offer offer = new Offer();
        assertNotNull(offer);
    }

    @Test
    public void testAllArgsConstructor() {
        Long id = 1L;
        String title = "Test Title";
        String description = "Test Description";
        String location = "Test Location";
        String salaryRange = "Test Salary Range";
        String technologies = "Test Technologies";

        Offer offer = new Offer(id, title, description, location, salaryRange, technologies);

        assertEquals(id, offer.getId());
        assertEquals(title, offer.getTitle());
        assertEquals(description, offer.getDescription());
        assertEquals(location, offer.getLocation());
        assertEquals(salaryRange, offer.getSalaryRange());
        assertEquals(technologies, offer.getTechnologies());
    }

    @Test
    public void testSettersAndGetters() {
        Long id = 1L;
        String title = "Test Title";
        String description = "Test Description";
        String location = "Test Location";
        String salaryRange = "Test Salary Range";
        String technologies = "Test Technologies";

        Offer offer = new Offer();
        offer.setId(id);
        offer.setTitle(title);
        offer.setDescription(description);
        offer.setLocation(location);
        offer.setSalaryRange(salaryRange);
        offer.setTechnologies(technologies);

        assertEquals(id, offer.getId());
        assertEquals(title, offer.getTitle());
        assertEquals(description, offer.getDescription());
        assertEquals(location, offer.getLocation());
        assertEquals(salaryRange, offer.getSalaryRange());
        assertEquals(technologies, offer.getTechnologies());
    }

    @Test
    public void testEqualsAndHashCode() {
        Offer offer1 = new Offer(1L, "Test Title", "Test Description", "Test Location", "Test Salary Range", "Test Technologies");
        Offer offer2 = new Offer(1L, "Test Title", "Test Description", "Test Location", "Test Salary Range", "Test Technologies");
        Offer offer3 = new Offer(2L, "Another Title", "Another Description", "Another Location", "Another Salary Range", "Another Technologies");

        assertEquals(offer1, offer2);
        assertNotEquals(offer1, offer3);

        assertEquals(offer1.hashCode(), offer2.hashCode());
        assertNotEquals(offer1.hashCode(), offer3.hashCode());
    }

    @Test
    public void testToString() {
        Offer offer = new Offer(1L, "Test Title", "Test Description", "Test Location", "Test Salary Range", "Test Technologies");
        String expectedString = "Offer(id=1, title=Test Title, description=Test Description, location=Test Location, salaryRange=Test Salary Range, technologies=Test Technologies)";

        assertEquals(expectedString, offer.toString());
    }
}