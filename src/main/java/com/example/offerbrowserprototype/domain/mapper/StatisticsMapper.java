package com.example.offerbrowserprototype.domain.mapper;

import com.example.offerbrowserprototype.domain.dto.statistics.CityDistributionDTO;
import com.example.offerbrowserprototype.domain.dto.statistics.LevelDistributionDTO;
import com.example.offerbrowserprototype.domain.dto.statistics.StatisticsSummaryDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class StatisticsMapper {

    public StatisticsSummaryDTO toDTO(long totalOffers) {
        return new StatisticsSummaryDTO(totalOffers );
    }

    public List<CityDistributionDTO> toCityDistributionDTOs(Map<String, Long> cityDistribution) {
        return cityDistribution.entrySet().stream()
                .map(entry -> new CityDistributionDTO(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    public List<LevelDistributionDTO> toLevelDistributionDTOs(Map<String, Long> levelDistribution) {
        return levelDistribution.entrySet().stream()
                .map(entry -> new LevelDistributionDTO(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }
}
