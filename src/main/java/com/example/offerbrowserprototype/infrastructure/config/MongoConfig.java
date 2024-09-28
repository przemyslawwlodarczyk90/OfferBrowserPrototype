package com.example.offerbrowserprototype.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.example.offerbrowserprototype.domain")
public class MongoConfig {}