package com.example.offerbrowserprototype.infrastructure.web;

import com.example.offerbrowserprototype.domain.dto.statistics.StatisticsSummaryDTO;
import com.example.offerbrowserprototype.domain.mapper.CityDistributionMapper;
import com.example.offerbrowserprototype.domain.mapper.LevelDistributionMapper;
import com.example.offerbrowserprototype.domain.mapper.StatisticsMapper;
import com.example.offerbrowserprototype.domain.statistics.StatisticsFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
@RestController
@RequestMapping("/api/statistics")
@Tag(name = "Statistics Controller", description = "Operations for retrieving statistics about job offers.")
@RequiredArgsConstructor
public class StatisticController {

    private final StatisticsFacade statisticsFacade;
    private final CityDistributionMapper cityDistributionMapper;
    private final LevelDistributionMapper levelDistributionMapper;
    private final StatisticsMapper statisticsMapper;

    @GetMapping("/total-offers")
    @Operation(summary = "Get total offers", description = "Returns the total number of job offers.")
    public ResponseEntity<StatisticsSummaryDTO> getTotalOffers() {
        long totalOffers = statisticsFacade.getTotalOffers();
        long appliedOffers = statisticsFacade.getAppliedOffers();
        return ResponseEntity.ok(statisticsMapper.toDTO(totalOffers, appliedOffers));
    }

    @GetMapping("/level-distribution")
    @Operation(summary = "Get level distribution", description = "Returns the distribution of job offers across levels.")
    public ResponseEntity<Map<String, Long>> getLevelDistribution() {
        Map<String, Long> levelDistribution = statisticsFacade.getLevelDistribution();
        return ResponseEntity.ok(levelDistributionMapper.toMap(levelDistribution));
    }

    @GetMapping("/city-distribution")
    @Operation(summary = "Get city distribution", description = "Returns the distribution of job offers by city.")
    public ResponseEntity<Map<String, Long>> getCityDistribution() {
        Map<String, Long> cityDistribution = statisticsFacade.getCityDistribution();
        return ResponseEntity.ok(cityDistributionMapper.toMap(cityDistribution));
    }
}
