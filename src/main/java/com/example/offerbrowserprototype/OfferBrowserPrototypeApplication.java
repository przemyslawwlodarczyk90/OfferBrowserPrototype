package com.example.offerbrowserprototype;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableScheduling
@EnableRetry
public class OfferBrowserPrototypeApplication {

    public static void main(String[] args) {
        SpringApplication.run(OfferBrowserPrototypeApplication.class, args);
    }
}