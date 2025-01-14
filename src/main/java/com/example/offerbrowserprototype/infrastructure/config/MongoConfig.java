package com.example.offerbrowserprototype.infrastructure.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ReadPreference;
import com.mongodb.client.MongoClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

@Configuration
public class MongoConfig {

    private static final Logger logger = LoggerFactory.getLogger(MongoConfig.class);

    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    @Bean
    public MongoTemplate mongoTemplate() {
        logger.info("Initializing MongoTemplate with URI: {}", mongoUri);

        ConnectionString connectionString = new ConnectionString(mongoUri);
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .readPreference(ReadPreference.primary())
                .build();

        try {
            MongoTemplate mongoTemplate = new MongoTemplate(
                    new SimpleMongoClientDatabaseFactory(MongoClients.create(mongoClientSettings), connectionString.getDatabase())
            );
            logger.info("MongoTemplate initialized successfully");
            return mongoTemplate;
        } catch (Exception e) {
            logger.error("Failed to initialize MongoTemplate", e);
            throw e;
        }
    }
}
