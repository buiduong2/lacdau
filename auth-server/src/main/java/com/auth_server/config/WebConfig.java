package com.auth_server.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

@EnableAsync
@EnableSpringDataWebSupport(pageSerializationMode = PageSerializationMode.VIA_DTO)
@Configuration
public class WebConfig {

    @Value("${custom.client.origins}")
    private List<String> clientOrigins;

    @Bean
    CorsConfigurationSource configurationSource() {
        return request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedOrigins(clientOrigins);
            config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTION"));
            config.setAllowedHeaders(List.of("*"));
            return config;
        };
    }
}