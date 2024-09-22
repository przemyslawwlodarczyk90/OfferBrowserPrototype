package com.example.offerbrowserprototype.domain.offer;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document(collection = "job_offers")
public class Offer {

    @Id
    private String id; // MongoDB automatycznie generuje unikalne ID

    private String title;

    private String description;

    private String location;

    private String salaryRange;

    private String technologies;
}
