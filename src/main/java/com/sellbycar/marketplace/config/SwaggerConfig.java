package com.sellbycar.marketplace.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
                        .url("http://16.171.118.114:8443/swagger-ui/index.html")
                        .description("Future Server for Marketplace"));
    }
}
