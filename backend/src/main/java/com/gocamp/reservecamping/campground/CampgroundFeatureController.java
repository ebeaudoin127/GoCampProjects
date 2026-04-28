// ============================================================
// Fichier : CampgroundFeatureController.java
// Dernière modification : 2026-04-17
// Auteur : ChatGPT
//
// Résumé :
// - Endpoints pour services / activités / hébergements
// - Endpoints pour les associations d’un camping
// ============================================================

package com.gocamp.reservecamping.campground;

import com.gocamp.reservecamping.campground.dto.UpdateCampgroundFeaturesRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/campgrounds")
@CrossOrigin(origins = "*")
public class CampgroundFeatureController {

    private final CampgroundFeatureService service;

    public CampgroundFeatureController(CampgroundFeatureService service) {
        this.service = service;
    }

    @GetMapping("/services")
    public ResponseEntity<?> getServices() {
        try {
            return ResponseEntity.ok(service.getServices());
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", safeMessage(ex)));
        }
    }

    @GetMapping("/activities")
    public ResponseEntity<?> getActivities() {
        try {
            return ResponseEntity.ok(service.getActivities());
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", safeMessage(ex)));
        }
    }

    @GetMapping("/accommodation-types")
    public ResponseEntity<?> getAccommodationTypes() {
        try {
            return ResponseEntity.ok(service.getAccommodationTypes());
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", safeMessage(ex)));
        }
    }

    @GetMapping("/{id}/features")
    public ResponseEntity<?> getCampgroundFeatures(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.getCampgroundFeatures(id));
        } catch (RuntimeException ex) {
            String message = safeMessage(ex);

            if (message.toLowerCase().contains("introuvable")) {
                return ResponseEntity.status(404).body(Map.of("error", message));
            }

            return ResponseEntity.badRequest().body(Map.of("error", message));
        }
    }

    @PutMapping("/{id}/features")
    public ResponseEntity<?> updateCampgroundFeatures(
            @PathVariable Long id,
            @RequestBody UpdateCampgroundFeaturesRequest req
    ) {
        try {
            return ResponseEntity.ok(service.updateCampgroundFeatures(id, req));
        } catch (RuntimeException ex) {
            String message = safeMessage(ex);

            if (message.toLowerCase().contains("introuvable")) {
                return ResponseEntity.status(404).body(Map.of("error", message));
            }

            return ResponseEntity.badRequest().body(Map.of("error", message));
        }
    }

    private String safeMessage(RuntimeException ex) {
        return ex.getMessage() != null ? ex.getMessage() : "Erreur serveur";
    }
}