package com.finalproject.finsera.finsera.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {


    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Finsera API Documentation")
                        .version("1.0.0")
                        .description("Documentation for Finsera API using Springdoc OpenAPI and Swagger UI, Key Features :" +
                                "1. Authentication" +
                                "2. Informasi Saldo" +
                                "3. Profiling" +
                                "4. Transfer Sesama Bank"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("HTTP Server"),
                        new Server().url("https://finsera-api.site").description("HTTPS Server")
                ));


    }






}