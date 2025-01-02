package com.example.offerbrowserprototype.infrastructure.web;

import com.example.offerbrowserprototype.domain.statistics.StatisticsFacade;
import com.example.offerbrowserprototype.domain.statistics.StatisticsResponse;
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

    @Operation(summary = "Get main statistics", description = "Retrieves main statistics including total offers, level distribution, and city distribution.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Error while retrieving statistics")
    })
    @GetMapping("/main")
    public ResponseEntity<StatisticsResponse> getMainStatistics() {
        StatisticsResponse statistics = statisticsFacade.getMainStatistics();
        return ResponseEntity.ok(statistics);
    }

    @Operation(summary = "Get total offers", description = "Returns the total number of job offers where isDuplicate is false.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Total offers count retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Error while retrieving total offers count")
    })
    @GetMapping("/total-offers")
    public ResponseEntity<Long> getTotalOffers() {
        long totalOffers = statisticsFacade.getTotalOffers();
        return ResponseEntity.ok(totalOffers);
    }

    @Operation(summary = "Get level distribution", description = "Returns the distribution of job offers across different levels (trainee, junior, mid, senior, etc.).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Level distribution retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Error while retrieving level distribution")
    })
    @GetMapping("/level-distribution")
    public ResponseEntity<Map<String, Double>> getLevelDistribution() {
        Map<String, Double> levelDistribution = statisticsFacade.getLevelDistribution();
        return ResponseEntity.ok(levelDistribution);
    }

    @Operation(summary = "Get city distribution", description = "Returns the number of job offers grouped by city.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "City distribution retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Error while retrieving city distribution")
    })
    @GetMapping("/city-distribution")
    public ResponseEntity<Map<String, Long>> getCityDistribution() {
        Map<String, Long> cityDistribution = statisticsFacade.getCityDistribution();
        return ResponseEntity.ok(cityDistribution);
    }
}
