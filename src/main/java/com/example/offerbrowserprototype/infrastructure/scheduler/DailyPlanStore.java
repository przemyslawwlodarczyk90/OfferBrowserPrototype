package com.example.offerbrowserprototype.infrastructure.scheduler;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

/**
 * Keeps the current and the previous day's plan in a small JSON file, so a restart
 * during the day keeps the drawn times and tomorrow's draw knows yesterday's slots.
 */
public class DailyPlanStore {

    private static final Logger logger = LoggerFactory.getLogger(DailyPlanStore.class);
    private static final ObjectMapper JSON = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .enable(SerializationFeature.INDENT_OUTPUT);

    public record State(DailyPlan current, DailyPlan previous) {}

    private final Path file;

    public DailyPlanStore(Path file) {
        this.file = file;
    }

    public Optional<State> load() {
        if (!Files.isRegularFile(file)) {
            return Optional.empty();
        }
        try {
            return Optional.of(JSON.readValue(file.toFile(), State.class));
        } catch (IOException e) {
            logger.warn("Cannot read schedule plan {} — a new one will be drawn: {}", file, e.getMessage());
            return Optional.empty();
        }
    }

    public void save(State state) {
        try {
            Path dir = file.toAbsolutePath().getParent();
            Files.createDirectories(dir);
            Path tmp = Files.createTempFile(dir, ".plan", ".tmp");
            JSON.writeValue(tmp.toFile(), state);
            Files.move(tmp, file, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException e) {
            logger.error("Cannot save schedule plan {}: {}", file, e.getMessage());
        }
    }
}
