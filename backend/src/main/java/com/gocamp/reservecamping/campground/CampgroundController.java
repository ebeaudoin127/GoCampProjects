// ============================================================
// Fichier : CampgroundController.java
// Dernière modification : 2026-05-04
// Auteur : ChatGPT
//
// Résumé :
// - Contrôleur backend du module campings
// - Endpoints pour créer, lister, consulter et modifier
// - Accessible aux utilisateurs connectés selon la configuration Security
// - Retourne les messages d'erreur propres au frontend
// ============================================================

package com.gocamp.reservecamping.campground;

import com.gocamp.reservecamping.campground.dto.CreateCampgroundRequest;
import com.gocamp.reservecamping.campground.dto.UpdateCampgroundRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/campgrounds")
@CrossOrigin(origins = "*")
public class CampgroundController {

    private final CampgroundService service;

    public CampgroundController(CampgroundService service) {
        this.service = service;
    }

    // ============================================================
    // LISTE
    // ============================================================
    @GetMapping
    public ResponseEntity<?> getAllCampgrounds() {
        try {
            return ResponseEntity.ok(service.getAllCampgrounds());
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", safeMessage(ex))
            );
        }
    }

    // ============================================================
    // DÉTAIL
    // ============================================================
    @GetMapping("/{id}")
    public ResponseEntity<?> getCampgroundById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.getCampgroundById(id));
        } catch (RuntimeException ex) {
            String message = safeMessage(ex);

            if (message.toLowerCase().contains("introuvable")) {
                return ResponseEntity.status(404).body(Map.of("error", message));
            }

            return ResponseEntity.badRequest().body(Map.of("error", message));
        }
    }

    // ============================================================
    // CRÉATION
    // ============================================================
    @PostMapping
    public ResponseEntity<?> createCampground(@RequestBody CreateCampgroundRequest req) {
        try {
            return ResponseEntity.ok(service.createCampground(req));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", safeMessage(ex))
            );
        }
    }

    // ============================================================
    // MODIFICATION
    // ============================================================
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCampground(
            @PathVariable Long id,
            @RequestBody UpdateCampgroundRequest req
    ) {
        try {
            return ResponseEntity.ok(service.updateCampground(id, req));
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
