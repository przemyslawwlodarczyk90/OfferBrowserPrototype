package com.example.offerbrowserprototype.domain.offer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfferDTO {
    private Long id;
    private String title;
    private String description;
    private String location;
    private String salaryRange;
    private String technologies;
}