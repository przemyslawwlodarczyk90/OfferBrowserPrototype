package com.offerbrowserprototype.domain.offer.dto;

import lombok.Data;

@Data
public class OfferDTO {
    private Long id;
    private String title;
    private String description;
    private String location;
    private String salaryRange;
    private String technologies;

}