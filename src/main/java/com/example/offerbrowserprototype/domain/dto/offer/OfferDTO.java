package com.example.offerbrowserprototype.domain.dto.offer;

import lombok.Data;

@Data
public class OfferDTO {
    private String id; // Zmień typ na `String`
    private String title;
    private String description;
    private String location;
    private String salaryRange;
    private String technologies;

    // Konstruktor bez ID (dla tworzenia nowych ofert)
    public OfferDTO(String title, String description, String location, String salaryRange, String technologies) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.salaryRange = salaryRange;
        this.technologies = technologies;
    }

    // Konstruktor z ID (dla istniejących ofert)
    public OfferDTO(String id, String title, String description, String location, String salaryRange, String technologies) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.salaryRange = salaryRange;
        this.technologies = technologies;
    }
}
