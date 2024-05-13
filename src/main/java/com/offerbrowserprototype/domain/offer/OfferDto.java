package com.offerbrowserprototype.domain.offer;

import lombok.Data;

@Data
public class OfferDto {
    private Long id;
    private String title;
    private String description;
    private String location;
}
