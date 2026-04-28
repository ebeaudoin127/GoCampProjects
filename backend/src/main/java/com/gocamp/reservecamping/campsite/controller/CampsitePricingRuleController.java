// ============================================================
// Fichier : backend/src/main/java/com/gocamp/reservecamping/campsite/controller/CampsitePricingRuleController.java
// Dernière modification : 2026-04-23
//
// Résumé :
// - API REST des règles tarifaires
// - Compatible avec dynamicBasePrice et daysOfWeek
// ============================================================

package com.gocamp.reservecamping.campsite.controller;

import com.gocamp.reservecamping.campsite.dto.CampsitePricingOverlapCheckResponse;
import com.gocamp.reservecamping.campsite.dto.CampsitePricingRuleResponse;
import com.gocamp.reservecamping.campsite.dto.CreateCampsitePricingRuleRequest;
import com.gocamp.reservecamping.campsite.dto.UpdateCampsitePricingRuleRequest;
import com.gocamp.reservecamping.campsite.service.CampsitePricingRuleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/campsite-pricing-rules")
@CrossOrigin("*")
public class CampsitePricingRuleController {

    private final CampsitePricingRuleService service;

    public CampsitePricingRuleController(CampsitePricingRuleService service) {
        this.service = service;
    }

    @GetMapping("/by-campground/{campgroundId}")
    public List<CampsitePricingRuleResponse> getByCampground(@PathVariable Long campgroundId) {
        return service.getByCampground(campgroundId);
    }

    @PostMapping("/preview")
    public CampsitePricingOverlapCheckResponse previewCreate(@RequestBody CreateCampsitePricingRuleRequest req) {
        return service.previewCreate(req);
    }

    @PostMapping
    public CampsitePricingRuleResponse create(@RequestBody CreateCampsitePricingRuleRequest req) {
        return service.create(req);
    }

    @PostMapping("/{id}/preview")
    public CampsitePricingOverlapCheckResponse previewUpdate(
            @PathVariable Long id,
            @RequestBody UpdateCampsitePricingRuleRequest req
    ) {
        return service.previewUpdate(id, req);
    }

    @PutMapping("/{id}")
    public CampsitePricingRuleResponse update(
            @PathVariable Long id,
            @RequestBody UpdateCampsitePricingRuleRequest req
    ) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
