package com.sellbycar.marketplace.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@Configuration
public class SwaggerConfig {

    @Bean
    OpenAPI customOpenApi() {
        return new OpenAPI().info(new Info().
                        title("RESTfull API marketplace")
                        .version("v1")
                        .description("REST API for user database")
                        .termsOfService("s"))
                .addServersItem(new Server()
                        .url("http://51.20.169.250/swagger-ui/index.html")
                        .description("Future Server for Marketplace"));
    }
}
