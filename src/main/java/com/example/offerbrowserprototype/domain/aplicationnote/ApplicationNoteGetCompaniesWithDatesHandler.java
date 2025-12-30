package com.example.offerbrowserprototype.domain.aplicationnote;

import com.example.offerbrowserprototype.infrastructure.repository.ApplicationNoteRepository;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ApplicationNoteGetCompaniesWithDatesHandler {

    private final ApplicationNoteRepository applicationNoteRepository;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public ApplicationNoteGetCompaniesWithDatesHandler(ApplicationNoteRepository applicationNoteRepository) {
        this.applicationNoteRepository = applicationNoteRepository;
    }

    public Map<String, List<String>> getCompaniesWithApplicationDates(Long userId) {
        return applicationNoteRepository.findByUserId(userId).stream()
                .collect(Collectors.groupingBy(
                        ApplicationNote::getCompanyName,
                        Collectors.mapping(
                                n -> n.getAppliedAt().toLocalDate().toString(),
                                Collectors.toList()
                        )
                ));
    }
}
