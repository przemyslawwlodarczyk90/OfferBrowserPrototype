package com.example.offerbrowserprototype.infrastructure.facade;

import com.example.offerbrowserprototype.domain.aplicationnote.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class ApplicationNoteFacade {

    private final ApplicationNoteGetAllHandler getAllHandler;
    private final ApplicationNoteGetByCompanyNameHandler getByCompanyNameHandler;
    private final ApplicationNoteGetCompaniesWithDatesHandler getCompaniesWithDatesHandler;
    private final ExternalSourceApplicationNoteHandler externalSourceApplicationNoteHandler;
    private final ApplicationNoteCountHandler countHandler;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String ALL_NOTES_CACHE_KEY = "applicationNotes:all";
    private static final String NOTES_COUNT_CACHE_KEY = "applicationNotes:count";

    public ApplicationNoteFacade(
            ApplicationNoteGetAllHandler getAllHandler,
            ApplicationNoteGetByCompanyNameHandler getByCompanyNameHandler,
            ApplicationNoteGetCompaniesWithDatesHandler getCompaniesWithDatesHandler,
            ExternalSourceApplicationNoteHandler externalSourceApplicationNoteHandler,
            ApplicationNoteCountHandler countHandler,
            RedisTemplate<String, Object> redisTemplate) {
        this.getAllHandler = getAllHandler;
        this.getByCompanyNameHandler = getByCompanyNameHandler;
        this.getCompaniesWithDatesHandler = getCompaniesWithDatesHandler;
        this.externalSourceApplicationNoteHandler = externalSourceApplicationNoteHandler;
        this.countHandler = countHandler;
        this.redisTemplate = redisTemplate;
    }

    public List<ApplicationNote> getAllApplicationNotes() {

        List<ApplicationNote> cachedNotes = (List<ApplicationNote>) redisTemplate.opsForValue().get(ALL_NOTES_CACHE_KEY);
        if (cachedNotes != null) {
            return cachedNotes;
        }

        List<ApplicationNote> notes = getAllHandler.getAllNotes();
        redisTemplate.opsForValue().set(ALL_NOTES_CACHE_KEY, notes, 1, TimeUnit.HOURS);
        return notes;
    }

    public List<ApplicationNote> getApplicationNotesByCompanyName(String companyName) {
        return getByCompanyNameHandler.getNotesByCompanyName(companyName);
    }

    public Map<String, List<String>> getCompaniesWithApplicationDates() {
        return getCompaniesWithDatesHandler.getCompaniesWithApplicationDates();
    }

    public ApplicationNote createNoteForExternalSource(String companyName, String url) {
        ApplicationNote note = externalSourceApplicationNoteHandler.createNoteForExternalSource(companyName, url);

        redisTemplate.delete(ALL_NOTES_CACHE_KEY);
        redisTemplate.delete(NOTES_COUNT_CACHE_KEY);
        return note;
    }

    public long countAllApplicationNotes() {
        Long cachedCount = (Long) redisTemplate.opsForValue().get(NOTES_COUNT_CACHE_KEY);
        if (cachedCount != null) {
            return cachedCount;
        }


        long count = countHandler.countAllNotes();
        redisTemplate.opsForValue().set(NOTES_COUNT_CACHE_KEY, count, 1, TimeUnit.HOURS);
        return count;
    }
}
