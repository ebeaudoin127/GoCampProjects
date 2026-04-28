// ============================================================
// Fichier : CampsiteController.java
// Chemin : backend/src/main/java/com/gocamp/reservecamping/campsite/controller
// Dernière modification : 2026-04-18
//
// Résumé :
// - CRUD des sites
// - Ajout de la sauvegarde du polygone de carte
// ============================================================

package com.gocamp.reservecamping.campsite.controller;

import com.gocamp.reservecamping.campsite.dto.CreateCampsiteRequest;
import com.gocamp.reservecamping.campsite.dto.UpdateCampsiteMapShapeRequest;
import com.gocamp.reservecamping.campsite.dto.UpdateCampsiteRequest;
import com.gocamp.reservecamping.campsite.service.CampsiteService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/campsites")
@CrossOrigin("*")
public class CampsiteController {

    private final CampsiteService service;

    public CampsiteController(CampsiteService service) {
        this.service = service;
    }

    @PostMapping
    public Object create(@RequestBody CreateCampsiteRequest req) {
        return service.create(req);
    }

    @GetMapping("/by-campground/{campgroundId}")
    public Object getByCampground(@PathVariable Long campgroundId) {
        return service.getByCampground(campgroundId);
    }

    @GetMapping("/{id}")
    public Object getDetails(@PathVariable Long id) {
        return service.getDetails(id);
    }

    @PutMapping("/{id}")
    public Object update(@PathVariable Long id, @RequestBody UpdateCampsiteRequest req) {
        return service.update(id, req);
    }

    @PutMapping("/{id}/map-shape")
    public Object updateMapShape(@PathVariable Long id, @RequestBody UpdateCampsiteMapShapeRequest req) {
        service.updateMapShape(id, req);
        return java.util.Map.of("success", true);
    }
}
