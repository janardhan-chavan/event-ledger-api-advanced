package com.assignment.eventledger.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI eventLedgerOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("Event Ledger API")
                        .description("Spring Boot Event Ledger API Assignment")
                        .version("1.0"));
    }
}
