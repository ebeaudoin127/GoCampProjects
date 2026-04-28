// ============================================================
// Fichier : backend/src/main/java/com/gocamp/reservecamping/campsite/service/CampgroundSitePricingOptionService.java
// Dernière modification : 2026-04-20
//
// Résumé :
// - Service des valeurs de tarification définies par camping
// ============================================================

package com.gocamp.reservecamping.campsite.service;

import com.gocamp.reservecamping.campground.model.Campground;
import com.gocamp.reservecamping.campground.repository.CampgroundRepository;
import com.gocamp.reservecamping.campsite.dto.CampgroundSitePricingOptionResponse;
import com.gocamp.reservecamping.campsite.dto.CreateCampgroundSitePricingOptionRequest;
import com.gocamp.reservecamping.campsite.model.CampgroundSitePricingOption;
import com.gocamp.reservecamping.campsite.repository.CampgroundSitePricingOptionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CampgroundSitePricingOptionService {

    private final CampgroundRepository campgroundRepository;
    private final CampgroundSitePricingOptionRepository repository;

    public CampgroundSitePricingOptionService(
            CampgroundRepository campgroundRepository,
            CampgroundSitePricingOptionRepository repository
    ) {
        this.campgroundRepository = campgroundRepository;
        this.repository = repository;
    }

    public List<CampgroundSitePricingOptionResponse> getByCampground(Long campgroundId) {
        return repository.findByCampgroundIdAndIsActiveTrueOrderByNameAsc(campgroundId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public CampgroundSitePricingOptionResponse create(CreateCampgroundSitePricingOptionRequest req) {
        if (req.campgroundId() == null) {
            throw new RuntimeException("Le camping est requis.");
        }

        if (req.name() == null || req.name().isBlank()) {
            throw new RuntimeException("Le nom de la valeur de tarification est requis.");
        }

        Campground campground = campgroundRepository.findById(req.campgroundId())
                .orElseThrow(() -> new RuntimeException("Camping introuvable."));

        CampgroundSitePricingOption option = new CampgroundSitePricingOption();
        option.setCampground(campground);
        option.setName(req.name().trim());
        option.setActive(req.isActive() == null || req.isActive());

        repository.save(option);

        return toResponse(option);
    }

    private CampgroundSitePricingOptionResponse toResponse(CampgroundSitePricingOption option) {
        return new CampgroundSitePricingOptionResponse(
                option.getId(),
                option.getCampground().getId(),
                option.getName(),
                option.isActive()
        );
    }
}