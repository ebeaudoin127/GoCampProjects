// ============================================================
// Fichier : CampgroundMapController.java
// Chemin : campmap/controller
// ============================================================

package com.gocamp.reservecamping.campmap.controller;

import com.gocamp.reservecamping.campmap.service.CampgroundMapService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/campgrounds")
@CrossOrigin("*")
public class CampgroundMapController {

    private final CampgroundMapService service;

    public CampgroundMapController(CampgroundMapService service) {
        this.service = service;
    }

    @GetMapping("/{id}/map")
    public Object getMap(@PathVariable Long id) {
        return service.getMap(id);
    }
}