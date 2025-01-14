package com.example.offerbrowserprototype;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoReactiveAutoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoReactiveAutoConfiguration.class})
@EnableMongoRepositories(basePackages = "com.example.offerbrowserprototype.infrastructure.repository")
@EnableScheduling
public class OfferBrowserPrototypeApplication {

    public static void main(String[] args) {
        SpringApplication.run(OfferBrowserPrototypeApplication.class, args);
    }

    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    @PostConstruct
    public void logMongoUri() {
        System.out.println("MongoDB URI: " + mongoUri);
    }
}
