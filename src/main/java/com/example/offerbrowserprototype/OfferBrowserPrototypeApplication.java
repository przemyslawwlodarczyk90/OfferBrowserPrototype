package com.example.offerbrowserprototype;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories(basePackages = "com.example.offerbrowserprototype.infrastructure.repository")
@SpringBootApplication
public class OfferBrowserPrototypeApplication {

    public static void main(String[] args) {
        SpringApplication.run(OfferBrowserPrototypeApplication.class, args);
    }

}
