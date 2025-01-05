package com.example.offerbrowserprototype.domain.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CityDistributionDTO {
    private String city;
    private long count;
}
