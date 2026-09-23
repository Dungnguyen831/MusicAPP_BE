package com.musicapp.musicBE.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * BE-1.7: Global CORS configuration.
 * Allows Flutter Web, Android emulator (10.0.2.2) and any local dev client
 * to reach all /api/v1/** endpoints. Tighten origins for production.
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                // Dev: allow all origins.
                // Prod: replace "*" with specific domains, e.g. "https://musicapp.example.com"
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                .allowedHeaders("*")
                // Allow Authorization header for future JWT usage
                .exposedHeaders("Content-Range", "Accept-Ranges", "Content-Length", "Authorization")
                .allowCredentials(false)   // must be false when allowedOriginPatterns("*")
                .maxAge(3600);             // preflight cache: 1 hour
    }
}
