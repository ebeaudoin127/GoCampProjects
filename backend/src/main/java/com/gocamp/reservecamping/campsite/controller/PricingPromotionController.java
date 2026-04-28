// ============================================================
// Fichier : PricingPromotionController.java
// Dernière modification : 2026-04-20
//
// Résumé :
// - API REST pour les promotions
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

    @PostMapping
    public PricingPromotionResponse create(@RequestBody CreatePricingPromotionRequest req) {
        return service.create(req);
    }

    @GetMapping("/by-campground/{campgroundId}")
    public List<PricingPromotionResponse> getByCampground(@PathVariable Long campgroundId) {
        return service.getByCampground(campgroundId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
