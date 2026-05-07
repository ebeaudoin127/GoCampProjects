// ============================================================
// Fichier : SearchAvailabilityController.java
// Chemin  : backend/src/main/java/com/gocamp/reservecamping/searchavailability/controller
// Dernière modification : 2026-05-07
// Auteur : ChatGPT pour Eric Beaudoin
//
// Résumé :
// - Controller REST du moteur de recherche disponibilité
// - Recherche géographique des campgrounds
//
// Historique des modifications :
// 2026-05-07
// - Création initiale
// ============================================================

package com.gocamp.reservecamping.searchavailability.controller;

import com.gocamp.reservecamping.searchavailability.repository.NearbyCampgroundProjection;
import com.gocamp.reservecamping.searchavailability.service.SearchAvailabilityService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/searchavailability")
public class SearchAvailabilityController {

    private final SearchAvailabilityService service;

    public SearchAvailabilityController(
            SearchAvailabilityService service
    ) {
        this.service = service;
    }

    @GetMapping("/nearby-campgrounds")
    public List<NearbyCampgroundProjection> findNearbyCampgrounds(
            @RequestParam BigDecimal latitude,
            @RequestParam BigDecimal longitude,
            @RequestParam BigDecimal radiusKm
    ) {

        return service.findNearbyCampgrounds(
                latitude,
                longitude,
                radiusKm
        );
    }
}
