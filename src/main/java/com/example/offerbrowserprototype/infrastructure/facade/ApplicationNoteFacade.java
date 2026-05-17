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
    private final ApplicationNoteHandler applicationNoteHandler;
    private final ApplicationNoteCountHandler countHandler;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String ALL_NOTES_CACHE_KEY_PREFIX = "applicationNotes:all:";
    private static final String NOTES_COUNT_CACHE_KEY_PREFIX = "applicationNotes:count:";

    public ApplicationNoteFacade(
            ApplicationNoteGetAllHandler getAllHandler,
            ApplicationNoteGetByCompanyNameHandler getByCompanyNameHandler,
            ApplicationNoteGetCompaniesWithDatesHandler getCompaniesWithDatesHandler,
            ExternalSourceApplicationNoteHandler externalSourceApplicationNoteHandler,
            ApplicationNoteHandler applicationNoteHandler,
            ApplicationNoteCountHandler countHandler,
            RedisTemplate<String, Object> redisTemplate
    ) {
        this.getAllHandler = getAllHandler;
        this.getByCompanyNameHandler = getByCompanyNameHandler;
        this.getCompaniesWithDatesHandler = getCompaniesWithDatesHandler;
        this.externalSourceApplicationNoteHandler = externalSourceApplicationNoteHandler;
        this.applicationNoteHandler = applicationNoteHandler;
        this.countHandler = countHandler;
        this.redisTemplate = redisTemplate;
    }

    public List<ApplicationNote> getAllApplicationNotes(Long userId) {
        String cacheKey = ALL_NOTES_CACHE_KEY_PREFIX + userId;

        @SuppressWarnings("unchecked")
        List<ApplicationNote> cachedNotes =
                (List<ApplicationNote>) redisTemplate.opsForValue().get(cacheKey);

        if (cachedNotes != null) {
            return cachedNotes;
        }

        List<ApplicationNote> notes = getAllHandler.getAllNotes(userId);
        redisTemplate.opsForValue().set(cacheKey, notes, 1, TimeUnit.HOURS);
        return notes;
    }

    public List<ApplicationNote> getApplicationNotesByCompanyName(Long userId, String companyName) {
        return getByCompanyNameHandler.getNotesByCompanyName(userId, companyName);
    }

    public Map<String, List<String>> getCompaniesWithApplicationDates(Long userId) {
        return getCompaniesWithDatesHandler.getCompaniesWithApplicationDates(userId);
    }

    public ApplicationNote saveNote(Long userId, Long offerId, String companyName, String offerUrl) {
        ApplicationNote note = applicationNoteHandler.saveApplicationNote(userId, offerId, offerUrl, companyName);
        redisTemplate.delete(ALL_NOTES_CACHE_KEY_PREFIX + userId);
        redisTemplate.delete(NOTES_COUNT_CACHE_KEY_PREFIX + userId);
        return note;
    }

    public ApplicationNote createNoteForExternalSource(
            Long userId,
            String companyName,
            String offerUrl
    ) {
        ApplicationNote note =
                externalSourceApplicationNoteHandler.createNoteForExternalSource(
                        userId, companyName, offerUrl
                );

        // invalidate cache
        redisTemplate.delete(ALL_NOTES_CACHE_KEY_PREFIX + userId);
        redisTemplate.delete(NOTES_COUNT_CACHE_KEY_PREFIX + userId);

        return note;
    }

    public long countAllApplicationNotes(Long userId) {
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
