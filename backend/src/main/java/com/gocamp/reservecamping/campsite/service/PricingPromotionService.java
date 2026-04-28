// ============================================================
// Fichier : PricingPromotionService.java
// Dernière modification : 2026-04-20
//
// Résumé :
// - Gestion des promotions
// - Création / suppression / lecture
// ============================================================

package com.gocamp.reservecamping.campsite.service;

import com.gocamp.reservecamping.campground.repository.CampgroundRepository;
import com.gocamp.reservecamping.campsite.dto.CreatePricingPromotionRequest;
import com.gocamp.reservecamping.campsite.dto.PricingPromotionResponse;
import com.gocamp.reservecamping.campsite.model.*;
import com.gocamp.reservecamping.campsite.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class PricingPromotionService {

    private final PricingPromotionRepository promotionRepository;
    private final PricingPromotionDayRepository dayRepository;
    private final CampgroundRepository campgroundRepository;
    private final CampsiteRepository campsiteRepository;
    private final CampgroundSitePricingOptionRepository pricingOptionRepository;

    public PricingPromotionService(
            PricingPromotionRepository promotionRepository,
            PricingPromotionDayRepository dayRepository,
            CampgroundRepository campgroundRepository,
            CampsiteRepository campsiteRepository,
            CampgroundSitePricingOptionRepository pricingOptionRepository
    ) {
        this.promotionRepository = promotionRepository;
        this.dayRepository = dayRepository;
        this.campgroundRepository = campgroundRepository;
        this.campsiteRepository = campsiteRepository;
        this.pricingOptionRepository = pricingOptionRepository;
    }

    public PricingPromotionResponse create(CreatePricingPromotionRequest req) {

        PricingPromotion promo = new PricingPromotion();

        promo.setCampground(
                campgroundRepository.findById(req.campgroundId())
                        .orElseThrow(() -> new RuntimeException("Camping introuvable"))
        );

        promo.setTargetType(req.targetType());
        promo.setApplicationMode(req.applicationMode());
        promo.setPromotionType(req.promotionType());

        if (req.campsiteId() != null) {
            promo.setCampsite(
                    campsiteRepository.findById(req.campsiteId())
                            .orElseThrow(() -> new RuntimeException("Site introuvable"))
            );
        }

        if (req.pricingOptionId() != null) {
            promo.setPricingOption(
                    pricingOptionRepository.findById(req.pricingOptionId())
                            .orElseThrow(() -> new RuntimeException("Option tarifaire invalide"))
            );
        }

        promo.setStartDate(req.startDate());
        promo.setEndDate(req.endDate());
        promo.setName(req.name());
        promo.setDescription(req.description());

        promo.setFixedPrice(req.fixedPrice());
        promo.setDiscountPercent(req.discountPercent());

        promo.setBuyNights(req.buyNights());
        promo.setPayNights(req.payNights());

        promo.setMinNights(req.minNights());
        promo.setPriority(req.priority() != null ? req.priority() : 100);

        promotionRepository.save(promo);

        if (req.days() != null) {
            for (PricingDayOfWeek day : req.days()) {
                PricingPromotionDay d = new PricingPromotionDay();
                d.setPricingPromotion(promo);
                d.setDayOfWeek(day);
                dayRepository.save(d);
            }
        }

        return map(promo);
    }

    public List<PricingPromotionResponse> getByCampground(Long campgroundId) {
        return promotionRepository
                .findByCampgroundIdOrderByPriorityAscStartDateAscEndDateAsc(campgroundId)
                .stream()
                .map(this::map)
                .toList();
    }

    public void delete(Long id) {
        promotionRepository.deleteById(id);
    }

    private PricingPromotionResponse map(PricingPromotion p) {

        List<PricingDayOfWeek> days = dayRepository.findByPricingPromotionId(p.getId())
                .stream()
                .map(PricingPromotionDay::getDayOfWeek)
                .toList();

        return new PricingPromotionResponse(
                p.getId(),
                p.getName(),
                p.getTargetType(),
                p.getApplicationMode(),
                p.getPromotionType(),
                p.getStartDate(),
                p.getEndDate(),
                p.getFixedPrice(),
                p.getDiscountPercent(),
                p.getBuyNights(),
                p.getPayNights(),
                p.getPriority(),
                days
        );
    }
}