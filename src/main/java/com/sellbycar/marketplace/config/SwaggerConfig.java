package com.sellbycar.marketplace.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Value("${SERVER_SOURCE}")
    private String serverURL;

    @Bean
    OpenAPI customOpenApi() {
        return new OpenAPI()
                .info(new Info().
                        title("REST API marketplace")
                        .version("v1")
                        .description("REST API for user database")
                        .termsOfService("s"))
                .addServersItem(new Server()
                        .url(serverURL)
                        .description("Future Server for Marketplace"));
    }
}
