package com.example.offerbrowserprototype.domain.cv;

import com.example.offerbrowserprototype.domain.exception.NotFoundException;
import com.example.offerbrowserprototype.domain.exception.UserNotFoundException;
import com.example.offerbrowserprototype.domain.user.User;
import com.example.offerbrowserprototype.infrastructure.repository.UserRepository;
import com.example.offerbrowserprototype.infrastructure.service.CvStorageService;
import com.example.offerbrowserprototype.infrastructure.service.CvStorageService.StoredCv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Optional;

/**
 * One CV per user: uploading replaces the previous file. The database holds only
 * the file location ({@code users.cv_url}); the file itself lives on disk.
 */
@Component
public class UserCvHandler {

    private static final Logger logger = LoggerFactory.getLogger(UserCvHandler.class);

    private final UserRepository userRepository;
    private final CvStorageService storage;

    public UserCvHandler(UserRepository userRepository, CvStorageService storage) {
        this.userRepository = userRepository;
        this.storage = storage;
    }

    public StoredCv upload(String username, String originalName, long size, InputStream content) throws IOException {
        User user = user(username);
        String previous = user.getCvUrl();
        String url = storage.store(user.getId(), originalName, size, content);
        try {
            user.setCvUrl(url);
            userRepository.save(user);
        } catch (RuntimeException e) {
            storage.delete(url); // the new file is not referenced by anyone — do not leave it behind
            throw e;
        }
        if (previous != null && !previous.equals(url)) {
            storage.delete(previous);
        }
        logger.info("User {} uploaded a CV ({})", username, url);
        return storage.find(url).orElseThrow(() -> new IllegalStateException("Zapisany plik CV zniknął: " + url));
    }

    /** CV of the user; empty when none was uploaded or the file is gone from disk. */
    public Optional<StoredCv> info(String username) {
        return storage.find(user(username).getCvUrl());
    }

    public Path file(String username) {
        StoredCv cv = info(username).orElseThrow(() -> new NotFoundException("Nie masz jeszcze wgranego CV"));
        return storage.path(cv.url());
    }

    public void delete(String username) {
        User user = user(username);
        String url = user.getCvUrl();
        if (url == null) {
            throw new NotFoundException("Nie masz jeszcze wgranego CV");
        }
        user.setCvUrl(null);
        userRepository.save(user);
        storage.delete(url);
        logger.info("User {} removed their CV ({})", username, url);
    }

    public long maxBytes() {
        return storage.maxBytes();
    }

    private User user(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Nie znaleziono użytkownika: " + username));
    }
}
