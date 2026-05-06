// ============================================================
// Fichier : PricingPromotionService.java
// Dernière modification : 2026-05-05
// Auteur : ChatGPT
//
// Résumé :
// - Gestion des promotions tarifaires dynamiques
// - Création / modification / suppression / lecture
// - Supporte SITE, GROUP, MULTI_CAMPSITE et ALL_CAMPGROUND
// - Supporte promo code, early booking, last minute,
//   jour d’arrivée obligatoire et fins de semaine consécutives
// ============================================================

package com.gocamp.reservecamping.campsite.service;

import com.gocamp.reservecamping.campground.repository.CampgroundRepository;
import com.gocamp.reservecamping.campsite.dto.CreatePricingPromotionRequest;
import com.gocamp.reservecamping.campsite.dto.PricingPromotionResponse;
import com.gocamp.reservecamping.campsite.model.Campsite;
import com.gocamp.reservecamping.campsite.model.CampgroundSitePricingOption;
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

    public PricingPromotionResponse create(CreatePricingPromotionRequest req) {
        validate(req);

        PricingPromotion promo = new PricingPromotion();
        applyRequestToEntity(promo, req);

        promotionRepository.save(promo);

        saveDays(promo, req.days());
        saveMultiCampsites(promo, req.campsiteIds());

        return map(promo);
    }

    public PricingPromotionResponse update(Long id, CreatePricingPromotionRequest req) {
        validate(req);

        PricingPromotion promo = promotionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promotion introuvable"));

        applyRequestToEntity(promo, req);

        promotionRepository.save(promo);

        dayRepository.deleteByPricingPromotionId(promo.getId());
        promotionCampsiteRepository.deleteByPricingPromotionId(promo.getId());

        saveDays(promo, req.days());
        saveMultiCampsites(promo, req.campsiteIds());

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
        dayRepository.deleteByPricingPromotionId(id);
        promotionCampsiteRepository.deleteByPricingPromotionId(id);
        promotionRepository.deleteById(id);
    }

    private void applyRequestToEntity(PricingPromotion promo, CreatePricingPromotionRequest req) {
        promo.setCampground(
                campgroundRepository.findById(req.campgroundId())
                        .orElseThrow(() -> new RuntimeException("Camping introuvable"))
        );

        promo.setTargetType(req.targetType());
        promo.setApplicationMode(req.applicationMode());
        promo.setPromotionType(req.promotionType());

        promo.setCampsite(null);
        promo.setPricingOption(null);

        if (req.targetType() == PricingTargetType.SITE && req.campsiteId() != null) {
            Campsite campsite = campsiteRepository.findById(req.campsiteId())
                    .orElseThrow(() -> new RuntimeException("Site introuvable"));

            promo.setCampsite(campsite);
        }

        if (req.targetType() == PricingTargetType.GROUP && req.pricingOptionId() != null) {
            CampgroundSitePricingOption option = pricingOptionRepository.findById(req.pricingOptionId())
                    .orElseThrow(() -> new RuntimeException("Regroupement tarifaire introuvable"));

            promo.setPricingOption(option);
        }

        promo.setStartDate(req.startDate());
        promo.setEndDate(req.endDate());

        promo.setName(req.name());
        promo.setDescription(req.description());

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

        promo.setPromoCode(normalizePromoCode(req.promoCode()));
        promo.setRequiresPromoCode(req.requiresPromoCode() != null && req.requiresPromoCode());
        promo.setBookingBeforeDate(req.bookingBeforeDate());
        promo.setArrivalWithinDays(req.arrivalWithinDays());
        promo.setRequiredArrivalDay(req.requiredArrivalDay());
    }

    private void saveDays(PricingPromotion promo, List<PricingDayOfWeek> days) {
        if (days == null || days.isEmpty()) {
            return;
        }

        for (PricingDayOfWeek day : days) {
            PricingPromotionDay promotionDay = new PricingPromotionDay();
            promotionDay.setPricingPromotion(promo);
            promotionDay.setDayOfWeek(day);
            dayRepository.save(promotionDay);
        }
    }

    private void saveMultiCampsites(PricingPromotion promo, List<Long> campsiteIds) {
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
                p.getPromoCode(),
                p.isRequiresPromoCode(),
                p.getBookingBeforeDate(),
                p.getArrivalWithinDays(),
                p.getRequiredArrivalDay(),
                days
        );
    }

    private void validate(CreatePricingPromotionRequest req) {
        if (req == null) {
            throw new RuntimeException("La requête est obligatoire");
        }

        if (req.campgroundId() == null) {
            throw new RuntimeException("Le camping est obligatoire");
        }

        if (req.name() == null || req.name().isBlank()) {
            throw new RuntimeException("Le nom de la promotion est obligatoire");
        }

        if (req.targetType() == null) {
            throw new RuntimeException("La cible de la promotion est obligatoire");
        }

        if (req.applicationMode() == null) {
            throw new RuntimeException("Le mode d'application est obligatoire");
        }

        if (req.promotionType() == null) {
            throw new RuntimeException("Le type de promotion est obligatoire");
        }

        if (req.startDate() == null || req.endDate() == null) {
            throw new RuntimeException("Les dates de promotion sont obligatoires");
        }

        if (req.endDate().isBefore(req.startDate())) {
            throw new RuntimeException("La date de fin doit être après ou égale à la date de début");
        }

        if (req.targetType() == PricingTargetType.SITE && req.campsiteId() == null) {
            throw new RuntimeException("Un site doit être sélectionné pour une promotion de type SITE");
        }

        if (req.targetType() == PricingTargetType.GROUP && req.pricingOptionId() == null) {
            throw new RuntimeException("Un regroupement tarifaire doit être sélectionné pour une promotion de type GROUP");
        }

        if (req.targetType() == PricingTargetType.MULTI_CAMPSITE
                && (req.campsiteIds() == null || req.campsiteIds().isEmpty())) {
            throw new RuntimeException("Au moins un site doit être sélectionné pour une promotion multi-sites");
        }

        validatePromotionType(req);
        validateMarketingConditions(req);
    }

    private void validatePromotionType(CreatePricingPromotionRequest req) {
        PromotionType type = req.promotionType();

        if (type == PromotionType.PERCENT_DISCOUNT
                && (req.discountPercent() == null || req.discountPercent().signum() <= 0)) {
            throw new RuntimeException("Le rabais en pourcentage doit être supérieur à 0");
        }

        if (type == PromotionType.AMOUNT_DISCOUNT
                && (req.discountAmount() == null || req.discountAmount().signum() <= 0)) {
            throw new RuntimeException("Le rabais en dollars doit être supérieur à 0");
        }

        if (type == PromotionType.FIXED_PRICE
                && (req.fixedPrice() == null || req.fixedPrice().signum() <= 0)) {
            throw new RuntimeException("Le prix fixe doit être supérieur à 0");
        }

        if (type == PromotionType.BUY_X_PAY_Y) {
            if (req.buyNights() == null || req.payNights() == null) {
                throw new RuntimeException("Les champs nuits achetées et nuits payées sont obligatoires");
            }

            if (req.buyNights() <= req.payNights()) {
                throw new RuntimeException("Le nombre de nuits achetées doit être supérieur au nombre de nuits payées");
            }
        }

        if (type == PromotionType.X_NIGHTS_FOR_AMOUNT) {
            if (req.packageNights() == null || req.packageNights() <= 0) {
                throw new RuntimeException("Le nombre de nuits du forfait est obligatoire");
            }

            if (req.packagePrice() == null || req.packagePrice().signum() <= 0) {
                throw new RuntimeException("Le prix du forfait est obligatoire");
            }
        }

        if (type == PromotionType.CONSECUTIVE_WEEKENDS) {
            if (req.requiredConsecutiveWeekends() == null || req.requiredConsecutiveWeekends() <= 0) {
                throw new RuntimeException("Le nombre de fins de semaine consécutives est obligatoire");
            }

            if (req.packagePrice() == null || req.packagePrice().signum() <= 0) {
                throw new RuntimeException("Le montant total de la promotion est obligatoire");
            }
        }

        if (type == PromotionType.PACKAGE
                && (req.packagePrice() == null || req.packagePrice().signum() <= 0)) {
            throw new RuntimeException("Le prix du forfait est obligatoire");
        }
    }

    private void validateMarketingConditions(CreatePricingPromotionRequest req) {
        if (req.requiresPromoCode() != null && req.requiresPromoCode()) {
            if (req.promoCode() == null || req.promoCode().isBlank()) {
                throw new RuntimeException("Un code promo est obligatoire si la promotion exige un code");
            }
        }

        if (req.arrivalWithinDays() != null && req.arrivalWithinDays() < 0) {
            throw new RuntimeException("Le nombre de jours last minute doit être positif");
        }

        if (req.promotionType() == PromotionType.CONSECUTIVE_WEEKENDS) {
            if (req.requiredArrivalDay() != null && req.requiredArrivalDay() != PricingDayOfWeek.FRIDAY) {
                throw new RuntimeException("Une promotion de fins de semaine consécutives doit commencer un vendredi");
            }
        }
    }

    private String normalizePromoCode(String promoCode) {
        if (promoCode == null || promoCode.isBlank()) {
            return null;
        }

        return promoCode.trim().toUpperCase();
    }
}