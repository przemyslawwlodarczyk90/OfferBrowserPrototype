package com.example.offerbrowserprototype.infrastructure.web;

import org.springframework.web.bind.annotation.RequestParam;
import io.swagger.v3.oas.annotations.Parameter;
import com.example.offerbrowserprototype.domain.offer.OfferSource;
import com.example.offerbrowserprototype.domain.dto.statistics.CityDistributionDTO;
import com.example.offerbrowserprototype.domain.dto.statistics.LevelBySourceDTO;
import com.example.offerbrowserprototype.domain.dto.statistics.LevelDistributionDTO;
import com.example.offerbrowserprototype.domain.dto.statistics.StatisticsSummaryDTO;
import com.example.offerbrowserprototype.domain.mapper.StatisticsMapper;
import com.example.offerbrowserprototype.infrastructure.facade.StatisticsFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@Tag(name = "Statistics", description = "Aggregated statistics about job offers in the database.")

@PreAuthorize("isAuthenticated()")
public class StatisticController {

    private final StatisticsFacade statisticsFacade;
    private final StatisticsMapper statisticsMapper;

    public StatisticController(StatisticsFacade statisticsFacade, StatisticsMapper statisticsMapper) {
        this.statisticsFacade = statisticsFacade;
        this.statisticsMapper = statisticsMapper;
    }

    @GetMapping("/total-offers")
    @Operation(summary = "Total offer count", description = "Returns the total number of job offers in the database.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Count returned"),
            @ApiResponse(responseCode = "401", description = "Not authenticated")
    })
    public ResponseEntity<StatisticsSummaryDTO> getTotalOffers(
            @Parameter(description = "Portal: NoFluff, TheProtocol or JustJoin; omit for all portals") @RequestParam(required = false) String source
    ) {
        long totalOffers = statisticsFacade.getTotalOffers(OfferSource.fromParam(source).orElse(null));

        return ResponseEntity.ok(statisticsMapper.toDTO(totalOffers));
    }

    @GetMapping("/level-distribution")
    @Operation(summary = "Offer distribution by seniority level", description = "Returns offer counts grouped by seniority level (Junior, Mid, Senior, etc.).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Distribution returned"),
            @ApiResponse(responseCode = "401", description = "Not authenticated")
    })
    public ResponseEntity<List<LevelDistributionDTO>> getLevelDistribution(
            @Parameter(description = "Portal: NoFluff, TheProtocol or JustJoin; omit for all portals") @RequestParam(required = false) String source
    ) {
        Map<String, Long> levelDistribution = statisticsFacade.getLevelDistribution(OfferSource.fromParam(source).orElse(null));
        return ResponseEntity.ok(statisticsMapper.toLevelDistributionDTOs(levelDistribution));
    }

    @GetMapping("/city-distribution")
    @Operation(summary = "Offer distribution by city", description = "Returns offer counts grouped by city.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Distribution returned"),
            @ApiResponse(responseCode = "401", description = "Not authenticated")
    })
    public ResponseEntity<List<CityDistributionDTO>> getCityDistribution(
            @Parameter(description = "Portal: NoFluff, TheProtocol or JustJoin; omit for all portals") @RequestParam(required = false) String source
    ) {
        Map<String, Long> cityDistribution = statisticsFacade.getCityDistribution(OfferSource.fromParam(source).orElse(null));
        return ResponseEntity.ok(statisticsMapper.toCityDistributionDTOs(cityDistribution));
    }

    @GetMapping("/level-distribution-by-source")
    @Operation(summary = "All portals: offers per experience level, summed and split by portal",
               description = "One row per level (Trainee, Junior, Mid, Senior, …, unknown last) with the total over all portals "
                       + "and the count from each portal (0 when it has none). Duplicates are excluded.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Distribution returned"),
            @ApiResponse(responseCode = "401", description = "Not authenticated")
    })
    public ResponseEntity<List<LevelBySourceDTO>> getLevelDistributionBySource() {
        List<LevelBySourceDTO> rows = statisticsFacade.getLevelDistributionBySource().entrySet().stream()
                .map(e -> new LevelBySourceDTO(e.getKey(),
                        e.getValue().values().stream().mapToLong(Long::longValue).sum(),
                        e.getValue()))
                .toList();
        return ResponseEntity.ok(rows);
    }
}
