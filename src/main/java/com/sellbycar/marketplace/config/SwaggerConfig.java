package com.sellbycar.marketplace.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI marketPlaceOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Marketplace  API")
                        .version("v1")
                        .description("API Definitions of the Marketplace project")
                        .license(new License().name("Apache 2.0").url("https://springdoc.org")));
    }
}
