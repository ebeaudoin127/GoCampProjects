package com.gocamp.reservecamping.campsite.controller;

import com.gocamp.reservecamping.campsite.service.CampsiteReferenceService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/campsite-references")
@CrossOrigin("*")
public class CampsiteReferenceController {

    private final CampsiteReferenceService service;

    public CampsiteReferenceController(CampsiteReferenceService service) {
        this.service = service;
    }

    @GetMapping("/site-types")
    public Object getSiteTypes() {
        return service.getSiteTypes();
    }

    @GetMapping("/site-service-types")
    public Object getSiteServiceTypes() {
        return service.getSiteServiceTypes();
    }

    @GetMapping("/site-amperages")
    public Object getSiteAmperages() {
        return service.getSiteAmperages();
    }

    @GetMapping("/equipment-allowed-types")
    public Object getEquipmentAllowedTypes() {
        return service.getEquipmentAllowedTypes();
    }

    @GetMapping("/site-surface-types")
    public Object getSiteSurfaceTypes() {
        return service.getSiteSurfaceTypes();
    }
}
