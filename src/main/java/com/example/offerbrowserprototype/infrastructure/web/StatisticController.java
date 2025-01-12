package com.example.offerbrowserprototype.infrastructure.web;

import com.example.offerbrowserprototype.domain.dto.statistics.CityDistributionDTO;
import com.example.offerbrowserprototype.domain.dto.statistics.LevelDistributionDTO;
import com.example.offerbrowserprototype.domain.dto.statistics.StatisticsSummaryDTO;
import com.example.offerbrowserprototype.domain.mapper.StatisticsMapper;
import com.example.offerbrowserprototype.infrastructure.facade.StatisticsFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api/statistics")
@Tag(name = "Statistics Controller", description = "Operations for retrieving statistics about job offers.")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class StatisticController {

    private final StatisticsFacade statisticsFacade;
    private final StatisticsMapper statisticsMapper;

    @GetMapping("/total-offers")
    @Operation(summary = "Get total offers", description = "Returns the total number of job offers.")
    public ResponseEntity<StatisticsSummaryDTO> getTotalOffers() {
        long totalOffers = statisticsFacade.getTotalOffers();

        return ResponseEntity.ok(statisticsMapper.toDTO(totalOffers));
    }

    @GetMapping("/level-distribution")
    @Operation(summary = "Get level distribution", description = "Returns the distribution of job offers across levels.")
    public ResponseEntity<List<LevelDistributionDTO>> getLevelDistribution() {
        Map<String, Long> levelDistribution = statisticsFacade.getLevelDistribution();
        return ResponseEntity.ok(statisticsMapper.toLevelDistributionDTOs(levelDistribution));
    }

    @GetMapping("/city-distribution")
    @Operation(summary = "Get city distribution", description = "Returns the distribution of job offers by city.")
    public ResponseEntity<List<CityDistributionDTO>> getCityDistribution() {
        Map<String, Long> cityDistribution = statisticsFacade.getCityDistribution();
        return ResponseEntity.ok(statisticsMapper.toCityDistributionDTOs(cityDistribution));
    }
}
