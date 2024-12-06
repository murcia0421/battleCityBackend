package com.battlecity.battle_city_backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull; // Import the NonNull annotation
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) { // Annotate the parameter
        // For development purposes, allowing all origins. Consider restricting this in
        // production.
        registry.addMapping("/**").allowedOrigins("*");

        // Alternatively, specify allowed origins explicitly:
        // registry.addMapping("/**").allowedOrigins("http://localhost:3000");
    }
}
