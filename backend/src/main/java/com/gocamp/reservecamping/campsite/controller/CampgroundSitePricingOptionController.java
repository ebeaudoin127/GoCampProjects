// ============================================================
// Fichier : backend/src/main/java/com/gocamp/reservecamping/campsite/controller/CampgroundSitePricingOptionController.java
// Dernière modification : 2026-05-05
//
// Résumé :
// - API REST des valeurs de tarification par camping
// - Création, lecture et suppression sécurisée
// ============================================================

package com.gocamp.reservecamping.campsite.controller;

import com.gocamp.reservecamping.campsite.dto.CreateCampgroundSitePricingOptionRequest;
import com.gocamp.reservecamping.campsite.service.CampgroundSitePricingOptionService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/campground-site-pricing-options")
@CrossOrigin("*")
public class CampgroundSitePricingOptionController {

    private final CampgroundSitePricingOptionService service;

    public CampgroundSitePricingOptionController(CampgroundSitePricingOptionService service) {
        this.service = service;
    }

    @GetMapping("/by-campground/{campgroundId}")
    public Object getByCampground(@PathVariable Long campgroundId) {
        return service.getByCampground(campgroundId);
    }

    @PostMapping
    public Object create(@RequestBody CreateCampgroundSitePricingOptionRequest req) {
        return service.create(req);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
