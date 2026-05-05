// ============================================================
// Fichier : PricingPromotionController.java
// Dernière modification : 2026-05-04
//
// Résumé :
// - API REST pour les promotions dynamiques
// - Création / modification / suppression / lecture
// ============================================================

package com.gocamp.reservecamping.campsite.controller;

import com.gocamp.reservecamping.campsite.dto.CreatePricingPromotionRequest;
import com.gocamp.reservecamping.campsite.dto.PricingPromotionResponse;
import com.gocamp.reservecamping.campsite.service.PricingPromotionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pricing-promotions")
@CrossOrigin("*")
public class PricingPromotionController {

    private final PricingPromotionService service;

    public PricingPromotionController(PricingPromotionService service) {
        this.service = service;
    }

    // ============================================================
    // CRÉATION
    // ============================================================
    @PostMapping
    public PricingPromotionResponse create(@RequestBody CreatePricingPromotionRequest req) {
        return service.create(req);
    }

    // ============================================================
    // MODIFICATION
    // ============================================================
    @PutMapping("/{id}")
    public PricingPromotionResponse update(
            @PathVariable Long id,
            @RequestBody CreatePricingPromotionRequest req
    ) {
        return service.update(id, req);
    }

    // ============================================================
    // LISTE PAR CAMPING
    // ============================================================
    @GetMapping("/by-campground/{campgroundId}")
    public List<PricingPromotionResponse> getByCampground(@PathVariable Long campgroundId) {
        return service.getByCampground(campgroundId);
    }

    // ============================================================
    // SUPPRESSION
    // ============================================================
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}