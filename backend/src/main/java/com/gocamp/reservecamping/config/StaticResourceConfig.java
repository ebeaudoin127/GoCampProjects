// ============================================================
// Fichier : StaticResourceConfig.java
// Chemin : backend/src/main/java/com/gocamp/reservecamping/config
// Dernière modification : 2026-04-18
//
// Résumé :
// - Expose explicitement le dossier uploads
// - Permet d’accéder aux fichiers via /uploads/**
// ============================================================

package com.gocamp.reservecamping.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path uploadDir = Paths.get("uploads").toAbsolutePath().normalize();
        String uploadPath = uploadDir.toUri().toString();

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(uploadPath);
    }
}