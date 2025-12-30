package com.example.offerbrowserprototype.domain.dto.offer;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class OfferDTO implements Serializable {

    private Long id;

    @NotBlank(message = "Title cannot be empty")
    private String title;

    @NotBlank(message = "Description cannot be empty")
    private String description;

    @NotBlank(message = "Location cannot be empty")
    private String location;

    @NotBlank(message = "Offer URL cannot be empty")
    private String offerUrl;

    private String salaryRange;

    private String company;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String level;


    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private LocalDateTime fetchedAt;

    public OfferDTO(String title, String description, String location, String offerUrl, String salaryRange, String company, String level, boolean applied, LocalDateTime fetchedAt) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.offerUrl = offerUrl;
        this.salaryRange = salaryRange;
        this.company = company;
        this.level = level;
        this.fetchedAt = fetchedAt;
    }

    public OfferDTO(Long id, String title, String description, String location, String offerUrl, String salaryRange, String company, String level, boolean applied, LocalDateTime fetchedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.offerUrl = offerUrl;
        this.salaryRange = salaryRange;
        this.company = company;
        this.level = level;
        this.fetchedAt = fetchedAt;
    }
}
