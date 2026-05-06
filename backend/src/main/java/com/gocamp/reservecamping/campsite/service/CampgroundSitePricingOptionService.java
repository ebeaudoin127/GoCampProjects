// ============================================================
// Fichier : backend/src/main/java/com/gocamp/reservecamping/campsite/service/CampgroundSitePricingOptionService.java
// Dernière modification : 2026-05-05
//
// Résumé :
// - Service des valeurs de tarification définies par camping
// - Création et lecture des valeurs tarifaires
// - Suppression sécurisée seulement si la valeur n’est pas utilisée
// ============================================================

package com.gocamp.reservecamping.campsite.service;

import com.gocamp.reservecamping.campground.model.Campground;
import com.gocamp.reservecamping.campground.repository.CampgroundRepository;
import com.gocamp.reservecamping.campsite.dto.CampgroundSitePricingOptionResponse;
import com.gocamp.reservecamping.campsite.dto.CreateCampgroundSitePricingOptionRequest;
import com.gocamp.reservecamping.campsite.model.CampgroundSitePricingOption;
import com.gocamp.reservecamping.campsite.repository.CampgroundSitePricingOptionRepository;
import com.gocamp.reservecamping.campsite.repository.CampsitePricingAssignmentRepository;
import com.gocamp.reservecamping.campsite.repository.CampsitePricingRuleRepository;
import com.gocamp.reservecamping.campsite.repository.PricingPromotionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CampgroundSitePricingOptionService {

    private final CampgroundRepository campgroundRepository;
    private final CampgroundSitePricingOptionRepository repository;
    private final CampsitePricingAssignmentRepository assignmentRepository;
    private final CampsitePricingRuleRepository pricingRuleRepository;
    private final PricingPromotionRepository promotionRepository;

    public CampgroundSitePricingOptionService(
            CampgroundRepository campgroundRepository,
            CampgroundSitePricingOptionRepository repository,
            CampsitePricingAssignmentRepository assignmentRepository,
            CampsitePricingRuleRepository pricingRuleRepository,
            PricingPromotionRepository promotionRepository
    ) {
        this.campgroundRepository = campgroundRepository;
        this.repository = repository;
        this.assignmentRepository = assignmentRepository;
        this.pricingRuleRepository = pricingRuleRepository;
        this.promotionRepository = promotionRepository;
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

    public void delete(Long id) {
        CampgroundSitePricingOption option = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Valeur de tarification introuvable."));

        validateCanDelete(option);

        repository.delete(option);
    }

    private void validateCanDelete(CampgroundSitePricingOption option) {
        Long pricingOptionId = option.getId();

        if (assignmentRepository.existsByPricingOptionId(pricingOptionId)) {
            throw new RuntimeException(
                    "Impossible de supprimer cette valeur de tarification, car elle est utilisée par au moins un site."
            );
        }

        if (pricingRuleRepository.existsByPricingOptionId(pricingOptionId)) {
            throw new RuntimeException(
                    "Impossible de supprimer cette valeur de tarification, car elle est utilisée par au moins une règle tarifaire."
            );
        }

        if (promotionRepository.existsByPricingOptionId(pricingOptionId)) {
            throw new RuntimeException(
                    "Impossible de supprimer cette valeur de tarification, car elle est utilisée par au moins une promotion."
            );
        }
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