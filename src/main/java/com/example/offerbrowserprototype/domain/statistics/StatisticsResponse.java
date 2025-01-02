package com.example.offerbrowserprototype.domain.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class StatisticsResponse {
    private long totalOffers;
    private Map<String, Double> levelDistribution;
    private Map<String, Long> cityDistribution;
}
