package com.example.offerbrowserprototype.infrastructure.facade;

import com.example.offerbrowserprototype.domain.aplicationnote.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ApplicationNoteFacade {

    private final ApplicationNoteGetAllHandler getAllHandler;
    private final ApplicationNoteGetByCompanyNameHandler getByCompanyNameHandler;
    private final ApplicationNoteGetCompaniesWithDatesHandler getCompaniesWithDatesHandler;
    private final ExternalSourceApplicationNoteHandler externalSourceApplicationNoteHandler;
    private final ApplicationNoteHandler applicationNoteHandler;
    private final ApplicationNoteCountHandler countHandler;

    public ApplicationNoteFacade(
            ApplicationNoteGetAllHandler getAllHandler,
            ApplicationNoteGetByCompanyNameHandler getByCompanyNameHandler,
            ApplicationNoteGetCompaniesWithDatesHandler getCompaniesWithDatesHandler,
            ExternalSourceApplicationNoteHandler externalSourceApplicationNoteHandler,
            ApplicationNoteHandler applicationNoteHandler,
            ApplicationNoteCountHandler countHandler
    ) {
        this.getAllHandler = getAllHandler;
        this.getByCompanyNameHandler = getByCompanyNameHandler;
        this.getCompaniesWithDatesHandler = getCompaniesWithDatesHandler;
        this.externalSourceApplicationNoteHandler = externalSourceApplicationNoteHandler;
        this.applicationNoteHandler = applicationNoteHandler;
        this.countHandler = countHandler;
    }

    public List<ApplicationNote> getAllApplicationNotes(Long userId) {
        return getAllHandler.getAllNotes(userId);
    }

    public List<ApplicationNote> getApplicationNotesByCompanyName(Long userId, String companyName) {
        return getByCompanyNameHandler.getNotesByCompanyName(userId, companyName);
    }

    public Map<String, List<String>> getCompaniesWithApplicationDates(Long userId) {
        return getCompaniesWithDatesHandler.getCompaniesWithApplicationDates(userId);
    }

    public ApplicationNote saveNote(Long userId, Long offerId, String companyName, String offerUrl) {
        return applicationNoteHandler.saveApplicationNote(userId, offerId, offerUrl, companyName);
    }

    public ApplicationNote createNoteForExternalSource(Long userId, String companyName, String offerUrl) {
        return externalSourceApplicationNoteHandler.createNoteForExternalSource(userId, companyName, offerUrl);
    }

    public long countAllApplicationNotes(Long userId) {
        return countHandler.countAllNotes(userId);
    }
}
