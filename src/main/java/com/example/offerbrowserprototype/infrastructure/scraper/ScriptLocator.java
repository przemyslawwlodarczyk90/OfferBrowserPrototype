package com.example.offerbrowserprototype.infrastructure.scraper;

import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;

/**
 * Resolves a configured script path: an existing file path (relative to the
 * working directory) wins, otherwise it is looked up on the classpath
 * (e.g. {@code scripts/scraper.py} → {@code target/classes/scripts/scraper.py}).
 */
@Component
public class ScriptLocator {

    public String resolve(String configured) {
        File direct = new File(configured);
        if (direct.isFile()) {
            return direct.getAbsolutePath();
        }
        URL resource = getClass().getClassLoader().getResource(configured);
        if (resource != null && "file".equals(resource.getProtocol())) {
            try {
                return Path.of(resource.toURI()).toString();
            } catch (URISyntaxException e) {
                throw new IllegalStateException("Niepoprawna ścieżka skryptu: " + resource, e);
            }
        }
        throw new IllegalStateException("Nie znaleziono skryptu Pythona: " + configured);
    }
}
