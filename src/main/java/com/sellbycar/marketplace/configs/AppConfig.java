package com.sellbycar.marketplace.configs;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List;

@Configuration
@EnableCaching
@EnableScheduling
public class AppConfig {
    @Bean
    public MappingJackson2HttpMessageConverter octetStreamJsonConverter() {
        var converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(List.of(new MediaType("application", "octet-stream")));
        return converter;
    }
}
