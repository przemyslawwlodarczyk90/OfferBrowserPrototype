package com.example.offerbrowserprototype.infrastructure.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ReadPreference;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

@Configuration
public class MongoConfig {

    @Bean
    public MongoTemplate mongoTemplate() {
        // Tworzenie połączenia z MongoDB
        ConnectionString connectionString = new ConnectionString("mongodb://admin:admin123@localhost:27017/offer_browser_db?authSource=admin");
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .readPreference(ReadPreference.primary()) // Wymuszenie preferencji odczytu
                .build();

        // Utworzenie MongoTemplate z poprawioną konfiguracją
        return new MongoTemplate(
                new SimpleMongoClientDatabaseFactory(MongoClients.create(mongoClientSettings), connectionString.getDatabase())
        );
    }
}
