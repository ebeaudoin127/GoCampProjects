// ============================================================
// Fichier : PricingPromotionService.java
// Dernière modification : 2026-05-04
//
// Résumé :
// - Gestion des promotions dynamiques
// - Création / modification / suppression / lecture
// - Supporte camping complet, regroupement tarifaire, site unique et multi-sites
// - Supporte %, montant fixe, prix fixe, nuits achetées/payées,
//   forfait X nuits pour X montant, fins de semaine consécutives
// ============================================================

package com.gocamp.reservecamping.campsite.service;

import com.gocamp.reservecamping.campground.repository.CampgroundRepository;
import com.gocamp.reservecamping.campsite.dto.CreatePricingPromotionRequest;
import com.gocamp.reservecamping.campsite.dto.PricingPromotionResponse;
import com.gocamp.reservecamping.campsite.model.Campsite;
import com.gocamp.reservecamping.campsite.model.PricingDayOfWeek;
import com.gocamp.reservecamping.campsite.model.PricingPromotion;
import com.gocamp.reservecamping.campsite.model.PricingPromotionCampsite;
import com.gocamp.reservecamping.campsite.model.PricingPromotionDay;
import com.gocamp.reservecamping.campsite.model.PricingTargetType;
import com.gocamp.reservecamping.campsite.model.PromotionType;
import com.gocamp.reservecamping.campsite.repository.CampgroundSitePricingOptionRepository;
import com.gocamp.reservecamping.campsite.repository.CampsiteRepository;
import com.gocamp.reservecamping.campsite.repository.PricingPromotionCampsiteRepository;
import com.gocamp.reservecamping.campsite.repository.PricingPromotionDayRepository;
import com.gocamp.reservecamping.campsite.repository.PricingPromotionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class PricingPromotionService {

    private final PricingPromotionRepository promotionRepository;
    private final PricingPromotionDayRepository dayRepository;
    private final PricingPromotionCampsiteRepository promotionCampsiteRepository;
    private final CampgroundRepository campgroundRepository;
    private final CampsiteRepository campsiteRepository;
    private final CampgroundSitePricingOptionRepository pricingOptionRepository;

    public PricingPromotionService(
            PricingPromotionRepository promotionRepository,
            PricingPromotionDayRepository dayRepository,
            PricingPromotionCampsiteRepository promotionCampsiteRepository,
            CampgroundRepository campgroundRepository,
            CampsiteRepository campsiteRepository,
            CampgroundSitePricingOptionRepository pricingOptionRepository
    ) {
        this.promotionRepository = promotionRepository;
        this.dayRepository = dayRepository;
        this.promotionCampsiteRepository = promotionCampsiteRepository;
        this.campgroundRepository = campgroundRepository;
        this.campsiteRepository = campsiteRepository;
        this.pricingOptionRepository = pricingOptionRepository;
    }

    // ============================================================
    // CRÉATION
    // ============================================================
    public PricingPromotionResponse create(CreatePricingPromotionRequest req) {
        validateRequest(req);

        PricingPromotion promo = new PricingPromotion();

        applyRequestToPromotion(promo, req);

        promotionRepository.save(promo);

        replaceDays(promo, req.days());
        replaceTargetCampsites(promo, req.campsiteIds());

        return map(promo);
    }

    // ============================================================
    // MODIFICATION
    // ============================================================
    public PricingPromotionResponse update(Long id, CreatePricingPromotionRequest req) {
        validateRequest(req);

        PricingPromotion promo = promotionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promotion introuvable"));

        applyRequestToPromotion(promo, req);

        promotionRepository.save(promo);

        replaceDays(promo, req.days());
        replaceTargetCampsites(promo, req.campsiteIds());

        return map(promo);
    }

    // ============================================================
    // LECTURE
    // ============================================================
    public List<PricingPromotionResponse> getByCampground(Long campgroundId) {
        return promotionRepository
                .findByCampgroundIdOrderByPriorityAscStartDateAscEndDateAsc(campgroundId)
                .stream()
                .map(this::map)
                .toList();
    }

    // ============================================================
    // SUPPRESSION
    // ============================================================
    public void delete(Long id) {
        promotionRepository.deleteById(id);
    }

    // ============================================================
    // MAPPING REQUEST -> ENTITY
    // ============================================================
    private void applyRequestToPromotion(PricingPromotion promo, CreatePricingPromotionRequest req) {
        promo.setCampground(
                campgroundRepository.findById(req.campgroundId())
                        .orElseThrow(() -> new RuntimeException("Camping introuvable"))
        );

        promo.setTargetType(req.targetType());
        promo.setApplicationMode(req.applicationMode());
        promo.setPromotionType(req.promotionType());

        promo.setCampsite(null);
        promo.setPricingOption(null);

        if (req.targetType() == PricingTargetType.SITE) {
            promo.setCampsite(
                    campsiteRepository.findById(req.campsiteId())
                            .orElseThrow(() -> new RuntimeException("Site introuvable"))
            );
        }

        if (req.targetType() == PricingTargetType.GROUP) {
            promo.setPricingOption(
                    pricingOptionRepository.findById(req.pricingOptionId())
                            .orElseThrow(() -> new RuntimeException("Option tarifaire invalide"))
            );
        }

        promo.setStartDate(req.startDate());
        promo.setEndDate(req.endDate());
        promo.setName(req.name().trim());
        promo.setDescription(trimToNull(req.description()));

        promo.setFixedPrice(req.fixedPrice());
        promo.setDiscountPercent(req.discountPercent());
        promo.setDiscountAmount(req.discountAmount());

        promo.setBuyNights(req.buyNights());
        promo.setPayNights(req.payNights());

        promo.setPackageNights(req.packageNights());
        promo.setPackagePrice(req.packagePrice());

        promo.setRequiredConsecutiveWeekends(req.requiredConsecutiveWeekends());

        promo.setMinNights(req.minNights());
        promo.setMaxNights(req.maxNights());

        promo.setPriority(req.priority() != null ? req.priority() : 100);
        promo.setCombinable(req.combinable() != null && req.combinable());
        promo.setActive(req.isActive() == null || req.isActive());
    }

    // ============================================================
    // JOURS APPLICABLES
    // ============================================================
    private void replaceDays(PricingPromotion promo, List<PricingDayOfWeek> days) {
        dayRepository.deleteByPricingPromotionId(promo.getId());

        if (days == null || days.isEmpty()) {
            return;
        }

        for (PricingDayOfWeek day : days) {
            PricingPromotionDay entity = new PricingPromotionDay();
            entity.setPricingPromotion(promo);
            entity.setDayOfWeek(day);
            dayRepository.save(entity);
        }
    }

    // ============================================================
    // CIBLES MULTI-SITES
    // ============================================================
    private void replaceTargetCampsites(PricingPromotion promo, List<Long> campsiteIds) {
        promotionCampsiteRepository.deleteByPricingPromotionId(promo.getId());

        if (promo.getTargetType() != PricingTargetType.MULTI_CAMPSITE) {
            return;
        }

        if (campsiteIds == null || campsiteIds.isEmpty()) {
            return;
        }

        for (Long campsiteId : campsiteIds) {
            Campsite campsite = campsiteRepository.findById(campsiteId)
                    .orElseThrow(() -> new RuntimeException("Site introuvable : " + campsiteId));

            PricingPromotionCampsite link = new PricingPromotionCampsite();
            link.setPricingPromotion(promo);
            link.setCampsite(campsite);

            promotionCampsiteRepository.save(link);
        }
    }

    // ============================================================
    // VALIDATION
    // ============================================================
    private void validateRequest(CreatePricingPromotionRequest req) {
        if (req == null) {
            throw new RuntimeException("La requête est obligatoire.");
        }

        if (req.campgroundId() == null) {
            throw new RuntimeException("Le camping est obligatoire.");
        }

        if (req.targetType() == null) {
            throw new RuntimeException("Le type de cible est obligatoire.");
        }

        if (req.applicationMode() == null) {
            throw new RuntimeException("Le mode d'application est obligatoire.");
        }

        if (req.promotionType() == null) {
            throw new RuntimeException("Le type de promotion est obligatoire.");
        }

        if (req.name() == null || req.name().trim().isEmpty()) {
            throw new RuntimeException("Le nom de la promotion est obligatoire.");
        }

        if (req.startDate() == null) {
            throw new RuntimeException("La date de début est obligatoire.");
        }

        if (req.endDate() == null) {
            throw new RuntimeException("La date de fin est obligatoire.");
        }

        if (req.endDate().isBefore(req.startDate())) {
            throw new RuntimeException("La date de fin doit être après ou égale à la date de début.");
        }

        validateTarget(req);
        validatePromotionType(req);
        validateNights(req);
    }

    private void validateTarget(CreatePricingPromotionRequest req) {
        if (req.targetType() == PricingTargetType.SITE && req.campsiteId() == null) {
            throw new RuntimeException("Un site doit être sélectionné pour une promotion ciblant un site.");
        }

        if (req.targetType() == PricingTargetType.GROUP && req.pricingOptionId() == null) {
            throw new RuntimeException("Un regroupement tarifaire doit être sélectionné.");
        }

        if (req.targetType() == PricingTargetType.MULTI_CAMPSITE
                && (req.campsiteIds() == null || req.campsiteIds().isEmpty())) {
            throw new RuntimeException("Au moins un site doit être sélectionné pour une promotion multi-sites.");
        }
    }

    private void validatePromotionType(CreatePricingPromotionRequest req) {
        PromotionType type = req.promotionType();

        if (type == PromotionType.PERCENT_DISCOUNT) {
            requirePositiveDecimal(req.discountPercent(), "Le pourcentage de rabais est obligatoire.");
            if (req.discountPercent().compareTo(BigDecimal.valueOf(100)) > 0) {
                throw new RuntimeException("Le pourcentage de rabais ne peut pas dépasser 100%.");
            }
        }

        if (type == PromotionType.AMOUNT_DISCOUNT) {
            requirePositiveDecimal(req.discountAmount(), "Le montant du rabais est obligatoire.");
        }

        if (type == PromotionType.FIXED_PRICE) {
            requirePositiveDecimal(req.fixedPrice(), "Le prix fixe est obligatoire.");
        }

        if (type == PromotionType.BUY_X_PAY_Y) {
            requirePositiveInteger(req.buyNights(), "Le nombre de nuits achetées est obligatoire.");
            requirePositiveInteger(req.payNights(), "Le nombre de nuits payées est obligatoire.");

            if (req.buyNights() <= req.payNights()) {
                throw new RuntimeException("Les nuits achetées doivent être plus grandes que les nuits payées.");
            }
        }

        if (type == PromotionType.X_NIGHTS_FOR_AMOUNT) {
            requirePositiveInteger(req.packageNights(), "Le nombre de nuits du forfait est obligatoire.");
            requirePositiveDecimal(req.packagePrice(), "Le prix du forfait est obligatoire.");
        }

        if (type == PromotionType.CONSECUTIVE_WEEKENDS) {
            requirePositiveInteger(req.requiredConsecutiveWeekends(), "Le nombre de fins de semaine consécutives est obligatoire.");
            requirePositiveDecimal(req.packagePrice(), "Le prix du forfait est obligatoire.");
        }

        if (type == PromotionType.PACKAGE) {
            requirePositiveDecimal(req.packagePrice(), "Le prix du forfait est obligatoire.");
        }
    }

    private void validateNights(CreatePricingPromotionRequest req) {
        if (req.minNights() != null && req.minNights() < 0) {
            throw new RuntimeException("Le minimum de nuits ne peut pas être négatif.");
        }

        if (req.maxNights() != null && req.maxNights() < 0) {
            throw new RuntimeException("Le maximum de nuits ne peut pas être négatif.");
        }

        if (req.minNights() != null
                && req.maxNights() != null
                && req.maxNights() < req.minNights()) {
            throw new RuntimeException("Le maximum de nuits doit être supérieur ou égal au minimum de nuits.");
        }
    }

    private void requirePositiveDecimal(BigDecimal value, String message) {
        if (value == null || value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException(message);
        }
    }

    private void requirePositiveInteger(Integer value, String message) {
        if (value == null || value <= 0) {
            throw new RuntimeException(message);
        }
    }

    // ============================================================
    // MAPPING ENTITY -> RESPONSE
    // ============================================================
    private PricingPromotionResponse map(PricingPromotion p) {
        List<PricingDayOfWeek> days = dayRepository.findByPricingPromotionId(p.getId())
                .stream()
                .map(PricingPromotionDay::getDayOfWeek)
                .toList();

        List<Long> campsiteIds = promotionCampsiteRepository.findByPricingPromotionId(p.getId())
                .stream()
                .map(link -> link.getCampsite().getId())
                .toList();

        return new PricingPromotionResponse(
                p.getId(),
                p.getName(),
                p.getDescription(),
                p.getTargetType(),
                p.getApplicationMode(),
                p.getPromotionType(),
                p.getCampsite() != null ? p.getCampsite().getId() : null,
                p.getPricingOption() != null ? p.getPricingOption().getId() : null,
                campsiteIds,
                p.getStartDate(),
                p.getEndDate(),
                p.getFixedPrice(),
                p.getDiscountPercent(),
                p.getDiscountAmount(),
                p.getBuyNights(),
                p.getPayNights(),
                p.getPackageNights(),
                p.getPackagePrice(),
                p.getRequiredConsecutiveWeekends(),
                p.getMinNights(),
                p.getMaxNights(),
                p.getPriority(),
                p.isCombinable(),
                p.isActive(),
                days
        );
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }

        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}