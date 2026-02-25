package com.edutech.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI edutechOpenApi() {
        return new OpenAPI().info(new Info()
                .title("Edutech API")
                .description("Student AI Career Guidance backend API")
                .version("v1"));
    }
}
