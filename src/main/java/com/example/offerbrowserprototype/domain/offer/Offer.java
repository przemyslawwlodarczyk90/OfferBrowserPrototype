package com.example.offerbrowserprototype.domain.offer;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Clock;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Document(collection = "job_offers")
public class Offer {

    @Id
    private String id;

    private String title;
    private String description;
    private String location;
    private String salaryRange;
    private String level;

    @Indexed(unique = true)
    private String offerUrl;

    private boolean applied;

    private boolean isDuplicate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", timezone = "UTC")
    private LocalDateTime fetchedAt;

    private String company;

    public Offer(String title, String description, String location, String salaryRange, String level, String company, Clock clock) {
        if (company == null || company.trim().isEmpty()) {
            throw new IllegalArgumentException("Company cannot be null or empty.");
        }
        this.title = title;
        this.description = description;
        this.location = location;
        this.salaryRange = salaryRange;
        this.level = level;
        this.company = company;
        this.applied = false;
        this.isDuplicate = false;
        this.fetchedAt = LocalDateTime.now(clock);
    }
}
