package com.example.offerbrowserprototype.domain.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LevelDistribution {
    private String id;
    private long count;
}
