// ============================================================
// Fichier : backend/src/main/java/com/gocamp/reservecamping/campsite/controller/CampsiteUnavailabilityController.java
// Dernière modification : 2026-04-20
//
// Résumé :
// - API REST pour gérer les périodes d’indisponibilité temporaires des sites
// ============================================================

package com.gocamp.reservecamping.campsite.controller;

import com.gocamp.reservecamping.campsite.dto.CreateCampsiteUnavailabilityRequest;
import com.gocamp.reservecamping.campsite.dto.UpdateCampsiteUnavailabilityRequest;
import com.gocamp.reservecamping.campsite.service.CampsiteUnavailabilityService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/campsite-unavailabilities")
@CrossOrigin("*")
public class CampsiteUnavailabilityController {

    private final CampsiteUnavailabilityService service;

    public CampsiteUnavailabilityController(CampsiteUnavailabilityService service) {
        this.service = service;
    }

    @GetMapping("/by-campsite/{campsiteId}")
    public Object getByCampsite(@PathVariable Long campsiteId) {
        return service.getByCampsite(campsiteId);
    }

    @PostMapping
    public Object create(@RequestBody CreateCampsiteUnavailabilityRequest req) {
        return service.create(req);
    }

    @PutMapping("/{id}")
    public Object update(@PathVariable Long id, @RequestBody UpdateCampsiteUnavailabilityRequest req) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    public Object delete(@PathVariable Long id) {
        service.delete(id);
        return java.util.Map.of("success", true);
    }
}