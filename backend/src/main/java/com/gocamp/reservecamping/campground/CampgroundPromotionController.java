
// ============================================================
// Fichier : CampgroundPromotionController.java
// Dernière modification : 2026-05-04
// Auteur : ChatGPT
//
// Résumé :
// - Contrôleur backend des promotions marketing d’un camping
// - Utilise la table campground_promotion
// - Ne touche pas au moteur pricing_promotion
// ============================================================

package com.gocamp.reservecamping.campground;

import com.gocamp.reservecamping.campground.dto.CampgroundPromotionResponse;
import com.gocamp.reservecamping.campground.dto.CreateCampgroundPromotionRequest;
import com.gocamp.reservecamping.campground.dto.UpdateCampgroundPromotionRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/campgrounds/{campgroundId}/promotions")
@CrossOrigin(origins = "*")
public class CampgroundPromotionController {

    private final CampgroundPromotionService service;

    public CampgroundPromotionController(CampgroundPromotionService service) {
        this.service = service;
    }

    @GetMapping
    public List<CampgroundPromotionResponse> getAll(@PathVariable Long campgroundId) {
        return service.getByCampground(campgroundId);
    }

    @PostMapping
    public CampgroundPromotionResponse create(
            @PathVariable Long campgroundId,
            @RequestBody CreateCampgroundPromotionRequest req
    ) {
        return service.create(campgroundId, req);
    }

    @PutMapping("/{id}")
    public CampgroundPromotionResponse update(
            @PathVariable Long campgroundId,
            @PathVariable Long id,
            @RequestBody UpdateCampgroundPromotionRequest req
    ) {
        return service.update(campgroundId, id, req);
    }

    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable Long campgroundId,
            @PathVariable Long id
    ) {
        service.delete(campgroundId, id);
    }
}