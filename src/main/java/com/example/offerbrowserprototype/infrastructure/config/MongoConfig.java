//package com.example.offerbrowserprototype.infrastructure.config;
//
//import com.mongodb.ConnectionString;
//import com.mongodb.MongoClientSettings;
//import com.mongodb.ReadPreference;
//import com.mongodb.client.MongoClients;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
//
//@Configuration
//public class MongoConfig {
//
//    private static final Logger logger = LoggerFactory.getLogger(MongoConfig.class);
//
//    @Value("${spring.data.mongodb.uri}")
//    private String mongoUri;
//
//    @Bean
//    public MongoTemplate mongoTemplate() {
//        logger.info("Starting MongoTemplate initialization.");
//        logger.debug("MongoDB URI: {}", mongoUri);
//
//        ConnectionString connectionString;
//        MongoClientSettings mongoClientSettings;
//
//        try {
//            logger.info("Parsing MongoDB URI.");
//            connectionString = new ConnectionString(mongoUri);
//            logger.debug("Parsed MongoDB connection string: {}", connectionString);
//
//            logger.info("Building MongoClientSettings.");
//            mongoClientSettings = MongoClientSettings.builder()
//                    .applyConnectionString(connectionString)
//                    .readPreference(ReadPreference.primary())
//                    .build();
//            logger.debug("MongoClientSettings built successfully: {}", mongoClientSettings);
//        } catch (Exception e) {
//            logger.error("Error while parsing MongoDB URI or building MongoClientSettings", e);
//            throw e;
//        }
//
//        try {
//            logger.info("Creating MongoTemplate instance.");
//            MongoTemplate mongoTemplate = new MongoTemplate(
//                    new SimpleMongoClientDatabaseFactory(MongoClients.create(mongoClientSettings), connectionString.getDatabase())
//            );
//            logger.info("MongoTemplate created successfully for database: {}", connectionString.getDatabase());
//            return mongoTemplate;
//        } catch (Exception e) {
//            logger.error("Failed to create MongoTemplate", e);
//            throw e;
//        }
//    }
//}
