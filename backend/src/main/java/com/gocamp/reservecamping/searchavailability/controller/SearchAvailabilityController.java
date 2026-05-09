// ============================================================
// Fichier : SearchAvailabilityController.java
// Chemin  : backend/src/main/java/com/gocamp/reservecamping/searchavailability/controller
// Dernière modification : 2026-05-09
// Auteur : ChatGPT pour Eric Beaudoin
//
// Résumé :
// - Controller REST du moteur de recherche disponibilité
// - Recherche géographique des campgrounds
// - Résumé intelligent des disponibilités
//
// Historique des modifications :
// 2026-05-07
// - Création initiale
//
// 2026-05-08
// - Ajout POST /summary
//
// 2026-05-09
// - Correction complète du controller pour exposer /api/searchavailability/summary
// ============================================================

package com.gocamp.reservecamping.searchavailability.controller;

import com.gocamp.reservecamping.searchavailability.dto.AvailableCampsiteSearchRequest;
import com.gocamp.reservecamping.searchavailability.dto.SearchAvailabilitySummaryResponse;
import com.gocamp.reservecamping.searchavailability.repository.NearbyCampgroundProjection;
import com.gocamp.reservecamping.searchavailability.service.SearchAvailabilityService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/searchavailability")
public class SearchAvailabilityController {

    private final SearchAvailabilityService service;

    public SearchAvailabilityController(SearchAvailabilityService service) {
        this.service = service;
    }

    @GetMapping("/nearby-campgrounds")
    public List<NearbyCampgroundProjection> findNearbyCampgrounds(
            @RequestParam BigDecimal latitude,
            @RequestParam BigDecimal longitude,
            @RequestParam BigDecimal radiusKm
    ) {
        return service.findNearbyCampgrounds(latitude, longitude, radiusKm);
    }

    @PostMapping("/summary")
    public SearchAvailabilitySummaryResponse searchSummary(
            @RequestBody AvailableCampsiteSearchRequest request
    ) {
        return service.buildSearchSummary(request);
    }
}