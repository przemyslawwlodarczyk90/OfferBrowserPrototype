package com.example.offerbrowserprototype.domain.aplicationnote;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ApplicationNoteFacade {

    private final ApplicationNoteGetAllHandler getAllHandler;
    private final ApplicationNoteGetByCompanyNameHandler getByCompanyNameHandler;
    private final ApplicationNoteGetCompaniesWithDatesHandler getCompaniesWithDatesHandler;
    private final ExternalSourceApplicationNoteHandler externalSourceApplicationNoteHandler;

    public ApplicationNoteFacade(
            ApplicationNoteGetAllHandler getAllHandler,
            ApplicationNoteGetByCompanyNameHandler getByCompanyNameHandler,
            ApplicationNoteGetCompaniesWithDatesHandler getCompaniesWithDatesHandler,
            ExternalSourceApplicationNoteHandler externalSourceApplicationNoteHandler) {
        this.getAllHandler = getAllHandler;
        this.getByCompanyNameHandler = getByCompanyNameHandler;
        this.getCompaniesWithDatesHandler = getCompaniesWithDatesHandler;
        this.externalSourceApplicationNoteHandler = externalSourceApplicationNoteHandler;
    }

    public List<ApplicationNote> getAllApplicationNotes() {
        return getAllHandler.getAllNotes();
    }

    public List<ApplicationNote> getApplicationNotesByCompanyName(String companyName) {
        return getByCompanyNameHandler.getNotesByCompanyName(companyName);
    }

    public Map<String, List<String>> getCompaniesWithApplicationDates() {
        return getCompaniesWithDatesHandler.getCompaniesWithApplicationDates();
    }

    public ApplicationNote createNoteForExternalSource(String companyName, String url) {
        return externalSourceApplicationNoteHandler.createNoteForExternalSource(companyName, url);
    }
}
