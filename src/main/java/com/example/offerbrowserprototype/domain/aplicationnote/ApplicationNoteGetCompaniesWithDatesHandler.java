package com.example.offerbrowserprototype.domain.aplicationnote;

import com.example.offerbrowserprototype.domain.aplicationnote.ApplicationNote;
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

    public Map<String, List<String>> getCompaniesWithApplicationDates() {
        List<ApplicationNote> notes = applicationNoteRepository.findAll();

        return notes.stream()
                .collect(Collectors.groupingBy(
                        ApplicationNote::getCompanyName,
                        Collectors.mapping(
                                note -> note.getAppliedAt().toLocalDate().format(DATE_FORMATTER),
                                Collectors.collectingAndThen(Collectors.toList(),
                                        dates -> dates.stream()
                                                .sorted((d1, d2) -> d2.compareTo(d1)) // Sortuj daty malejąco
                                                .collect(Collectors.toList()))
                        )
                ));
    }
}
