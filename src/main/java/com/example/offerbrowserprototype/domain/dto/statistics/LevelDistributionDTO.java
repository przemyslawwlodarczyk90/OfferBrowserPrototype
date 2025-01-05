package com.example.offerbrowserprototype.domain.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LevelDistributionDTO {
    private String level;
    private long count;
}
