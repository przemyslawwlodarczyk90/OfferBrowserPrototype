package com.example.offerbrowserprototype.infrastructure.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        // Definicja schematu autoryzacji
        SecurityScheme securityScheme = new SecurityScheme()
                .name("Bearer Authentication")
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");

        // Dodanie schematu autoryzacji do komponentów OpenAPI
        Components components = new Components()
                .addSecuritySchemes("bearerAuth", securityScheme);

        // Zdefiniowanie wymagania autoryzacji dla endpointów wymagających JWT
        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList("bearerAuth");

        // Utworzenie sekcji "Info" z tytułem, opisem, wersją i dodatkowymi informacjami
        Info apiInfo = new Info()
                .title("Offer Browser API")
                .description("This API allows users to register, authenticate, and manage job offers.\n\n"
                        + "### Features include:\n"
                        + "- User registration and confirmation\n"
                        + "- JWT-based authentication and authorization\n"
                        + "- Creating, updating, and managing job offers\n"
                        + "- Fetching job offers from external sources\n\n"
                        + "For any questions, please contact our support team.")
                .version("1.0.0")
                .license(new License().name("MIT License").url("https://opensource.org/licenses/MIT"));

        // Kompletne ustawienia OpenAPI
        return new OpenAPI()
                .components(components)
                .addSecurityItem(securityRequirement)
                .info(apiInfo);
    }
}
