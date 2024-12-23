package com.example.offerbrowserprototype.infrastructure.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@ConditionalOnProperty(name = "python.script.scheduler.enabled", havingValue = "true", matchIfMissing = false)
public class PythonScriptScheduler {

    private static final Logger logger = LoggerFactory.getLogger(PythonScriptScheduler.class);
    private final RestTemplate restTemplate;

    public PythonScriptScheduler(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    @Scheduled(cron = "${python.script.scheduler.cron}")
    public void runPythonScript() {
        String url = "http://localhost:8080/api/python-script/run";
        logger.info("Executing Python script via endpoint: {}", url);
        try {
            restTemplate.getForObject(url, String.class);
            logger.info("Python script executed successfully.");
        } catch (Exception e) {
            logger.error("Error during Python script execution: {}", e.getMessage());
        }
    }
}
