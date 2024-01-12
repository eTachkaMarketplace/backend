package com.sellbycar.marketplace.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Value("${server.url}")
    private String serverURL;
    @Value("${server.description}")
    private String serverDescription;

    @Bean
    OpenAPI customOpenApi() {
        return new OpenAPI()
                .info(new Info().
                        title("REST API eTachka Marketplace")
                        .version("v1")
                        .description("Documentation for access to the eTachka Marketplace resources via REST API.")
                        .termsOfService("https://etachka-marketplace.space/tos"))
                .addServersItem(new Server()
                        .url(serverURL)
                        .description(serverDescription));
    }
}
