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

    private static final String ALL_NOTES_CACHE_KEY_PREFIX = "applicationNotes:all:";
    private static final String NOTES_COUNT_CACHE_KEY_PREFIX = "applicationNotes:count:";

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

    public List<ApplicationNote> getAllApplicationNotes(String userId) {
        String cacheKey = ALL_NOTES_CACHE_KEY_PREFIX + userId;

        List<ApplicationNote> cachedNotes = (List<ApplicationNote>) redisTemplate.opsForValue().get(cacheKey);
        if (cachedNotes != null) {
            return cachedNotes;
        }

        List<ApplicationNote> notes = getAllHandler.getAllNotes(userId);
        redisTemplate.opsForValue().set(cacheKey, notes, 1, TimeUnit.HOURS);
        return notes;
    }

    public List<ApplicationNote> getApplicationNotesByCompanyName(String userId, String companyName) {
        return getByCompanyNameHandler.getNotesByCompanyName(userId, companyName);
    }

    public Map<String, List<String>> getCompaniesWithApplicationDates(String userId) {
        return getCompaniesWithDatesHandler.getCompaniesWithApplicationDates(userId);
    }

    public ApplicationNote createNoteForExternalSource(String userId, String companyName, String url) {
        ApplicationNote note = externalSourceApplicationNoteHandler.createNoteForExternalSource(userId, companyName, url);

        String allNotesCacheKey = ALL_NOTES_CACHE_KEY_PREFIX + userId;
        String notesCountCacheKey = NOTES_COUNT_CACHE_KEY_PREFIX + userId;

        redisTemplate.delete(allNotesCacheKey);
        redisTemplate.delete(notesCountCacheKey);
        return note;
    }

    public long countAllApplicationNotes(String userId) {
        String cacheKey = NOTES_COUNT_CACHE_KEY_PREFIX + userId;

        Long cachedCount = (Long) redisTemplate.opsForValue().get(cacheKey);
        if (cachedCount != null) {
            return cachedCount;
        }

        long count = countHandler.countAllNotes(userId);
        redisTemplate.opsForValue().set(cacheKey, count, 1, TimeUnit.HOURS);
        return count;
    }
}
