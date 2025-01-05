package com.example.offerbrowserprototype.domain.mapper;

import com.example.offerbrowserprototype.domain.dto.statistics.StatisticsSummaryDTO;
import org.springframework.stereotype.Component;

@Component
public class StatisticsMapper {

    public StatisticsSummaryDTO toDTO(long totalOffers, long appliedOffers) {
        return new StatisticsSummaryDTO(totalOffers, appliedOffers);
    }
}
