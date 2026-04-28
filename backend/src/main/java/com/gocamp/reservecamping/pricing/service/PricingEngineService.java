// ============================================================
// Fichier : backend/src/main/java/com/gocamp/reservecamping/pricing/service/PricingEngineService.java
// Dernière modification : 2026-04-23
//
// Résumé :
// - Moteur complet de calcul des prix
// - Priorité métier : PROMOTION > RÈGLE SITE > RÈGLE GROUP > FALLBACK
// - Gestion multi-nuits
// - Gestion jour + période
// - Supporte FIXED / DYNAMIC
// - Supporte FIXED_PRICE / PERCENT_DISCOUNT / BUY_X_PAY_Y
// - Ajout du calcul DYNAMIC basé sur dynamicBasePrice
// ============================================================

package com.gocamp.reservecamping.pricing.service;

import com.gocamp.reservecamping.campsite.model.Campsite;
import com.gocamp.reservecamping.campsite.model.CampsitePricingAssignment;
import com.gocamp.reservecamping.campsite.model.CampsitePricingRule;
import com.gocamp.reservecamping.campsite.model.PricingDayOfWeek;
import com.gocamp.reservecamping.campsite.model.PricingPromotion;
import com.gocamp.reservecamping.campsite.model.PricingPromotionDay;
import com.gocamp.reservecamping.campsite.model.PricingTargetType;
import com.gocamp.reservecamping.campsite.model.PricingType;
import com.gocamp.reservecamping.campsite.model.PromotionApplicationMode;
import com.gocamp.reservecamping.campsite.model.PromotionType;
import com.gocamp.reservecamping.campsite.repository.CampsitePricingAssignmentRepository;
import com.gocamp.reservecamping.campsite.repository.CampsiteRepository;
import com.gocamp.reservecamping.campsite.repository.PricingPromotionDayRepository;
import com.gocamp.reservecamping.campsite.repository.PricingPromotionRepository;
import com.gocamp.reservecamping.campsite.service.CampsitePricingRuleService;
import com.gocamp.reservecamping.pricing.dto.PriceCalculationRequest;
import com.gocamp.reservecamping.pricing.dto.PriceCalculationResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class PricingEngineService {

    private static final BigDecimal DEFAULT_PRICE = BigDecimal.valueOf(50).setScale(2, RoundingMode.HALF_UP);
    private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);

    private final CampsiteRepository campsiteRepository;
    private final CampsitePricingAssignmentRepository pricingAssignmentRepository;
    private final PricingPromotionRepository promotionRepository;
    private final PricingPromotionDayRepository promotionDayRepository;
    private final CampsitePricingRuleService campsitePricingRuleService;

    public PricingEngineService(
            CampsiteRepository campsiteRepository,
            CampsitePricingAssignmentRepository pricingAssignmentRepository,
            PricingPromotionRepository promotionRepository,
            PricingPromotionDayRepository promotionDayRepository,
            CampsitePricingRuleService campsitePricingRuleService
    ) {
        this.campsiteRepository = campsiteRepository;
        this.pricingAssignmentRepository = pricingAssignmentRepository;
        this.promotionRepository = promotionRepository;
        this.promotionDayRepository = promotionDayRepository;
        this.campsitePricingRuleService = campsitePricingRuleService;
    }

    public PriceCalculationResponse calculate(PriceCalculationRequest req) {
        validateRequest(req);

        Campsite site = campsiteRepository.findById(req.campsiteId())
                .orElseThrow(() -> new RuntimeException("Site introuvable."));

        CampsitePricingAssignment assignment = pricingAssignmentRepository.findByCampsiteId(site.getId()).orElse(null);
        Long pricingOptionId = assignment != null && assignment.getPricingOption() != null
                ? assignment.getPricingOption().getId()
                : null;

        List<CalculatedNight> nights = new ArrayList<>();

        for (int i = 0; i < req.nights(); i++) {
            LocalDate date = req.date().plusDays(i);
            PricingDayOfWeek pricingDay = PricingDayOfWeek.valueOf(date.getDayOfWeek().name());

            CampsitePricingRule baseRule = campsitePricingRuleService.findBestRuleForDate(
                    site,
                    pricingOptionId,
                    date,
                    pricingDay
            );

            BigDecimal basePrice = computeBaseRulePrice(baseRule, pricingDay);
            String baseSource = buildBaseSource(baseRule);
            String baseLabel = buildBaseLabel(baseRule);

            PricingPromotion promotion = findMatchingPromotion(
                    site,
                    pricingOptionId,
                    date,
                    pricingDay,
                    req.nights()
            );

            CalculatedNight night = new CalculatedNight();
            night.date = date;
            night.baseRule = baseRule;
            night.basePrice = normalizeMoney(basePrice);
            night.finalPrice = normalizeMoney(basePrice);
            night.source = baseSource;
            night.label = baseLabel;
            night.promotion = promotion;

            if (promotion != null && promotion.getPromotionType() != PromotionType.BUY_X_PAY_Y) {
                night.finalPrice = applyPromotionToBasePrice(night.basePrice, promotion);
                night.finalPrice = normalizeMoney(night.finalPrice);
                night.source = "PROMOTION";
                night.label = promotion.getName();
            }

            nights.add(night);
        }

        applyBuyXPayYPromotions(nights);

        List<PriceCalculationResponse.DailyPrice> breakdown = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

        for (CalculatedNight night : nights) {
            BigDecimal safePrice = normalizeMoney(night.finalPrice);

            breakdown.add(new PriceCalculationResponse.DailyPrice(
                    night.date,
                    safePrice,
                    night.source,
                    night.label
            ));

            total = total.add(safePrice);
        }

        return new PriceCalculationResponse(normalizeMoney(total), breakdown);
    }

    private void validateRequest(PriceCalculationRequest req) {
        if (req == null) {
            throw new RuntimeException("La requête de calcul est requise.");
        }
        if (req.campsiteId() == null) {
            throw new RuntimeException("Le site est requis.");
        }
        if (req.date() == null) {
            throw new RuntimeException("La date de début est requise.");
        }
        if (req.nights() == null || req.nights() <= 0) {
            throw new RuntimeException("Le nombre de nuits doit être supérieur à zéro.");
        }
    }

    private PricingPromotion findMatchingPromotion(
            Campsite site,
            Long pricingOptionId,
            LocalDate date,
            PricingDayOfWeek day,
            Integer stayNights
    ) {
        return promotionRepository
                .findByCampgroundIdOrderByPriorityAscStartDateAscEndDateAsc(site.getCampground().getId())
                .stream()
                .filter(PricingPromotion::isActive)
                .filter(p -> !date.isBefore(p.getStartDate()))
                .filter(p -> !date.isAfter(p.getEndDate()))
                .filter(p -> matchesPromotionTarget(p, site, pricingOptionId))
                .filter(p -> matchesPromotionDay(p, day))
                .filter(p -> matchesPromotionMinNights(p, stayNights))
                .min(Comparator.comparing(PricingPromotion::getPriority))
                .orElse(null);
    }

    private boolean matchesPromotionTarget(PricingPromotion promotion, Campsite site, Long pricingOptionId) {
        if (promotion.getTargetType() == PricingTargetType.SITE) {
            return promotion.getCampsite() != null
                    && promotion.getCampsite().getId().equals(site.getId());
        }

        if (promotion.getTargetType() == PricingTargetType.GROUP) {
            return promotion.getPricingOption() != null
                    && pricingOptionId != null
                    && promotion.getPricingOption().getId().equals(pricingOptionId);
        }

        return false;
    }

    private boolean matchesPromotionDay(PricingPromotion promotion, PricingDayOfWeek day) {
        List<PricingPromotionDay> days = promotionDayRepository.findByPricingPromotionId(promotion.getId());

        if (days == null || days.isEmpty()) {
            return true;
        }

        return days.stream().anyMatch(d -> d.getDayOfWeek() == day);
    }

    private boolean matchesPromotionMinNights(PricingPromotion promotion, Integer stayNights) {
        if (promotion.getMinNights() == null) {
            return true;
        }

        return stayNights != null && stayNights >= promotion.getMinNights();
    }

    private BigDecimal computeBaseRulePrice(CampsitePricingRule rule, PricingDayOfWeek day) {
        if (rule == null) {
            return DEFAULT_PRICE;
        }

        if (rule.getPricingType() == PricingType.FIXED) {
            return rule.getFixedPrice() != null ? rule.getFixedPrice() : DEFAULT_PRICE;
        }

        if (rule.getPricingType() == PricingType.DYNAMIC) {
            return computeDynamicRulePrice(rule, day);
        }

        return DEFAULT_PRICE;
    }

    private BigDecimal computeDynamicRulePrice(CampsitePricingRule rule, PricingDayOfWeek day) {
        BigDecimal min = rule.getDynamicMinPrice();
        BigDecimal base = rule.getDynamicBasePrice();
        BigDecimal max = rule.getDynamicMaxPrice();

        if (base == null) {
            if (min != null && max != null) {
                base = min.add(max).divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP);
            } else if (min != null) {
                base = min;
            } else if (max != null) {
                base = max;
            } else {
                return DEFAULT_PRICE;
            }
        }

        if (min == null) {
            min = base;
        }

        if (max == null) {
            max = base;
        }

        BigDecimal result;

        switch (day) {
            case MONDAY, TUESDAY, WEDNESDAY -> result = min;
            case THURSDAY, SUNDAY -> result = base;
            case FRIDAY, SATURDAY -> result = max;
            default -> result = base;
        }

        if (result.compareTo(min) < 0) {
            result = min;
        }

        if (result.compareTo(max) > 0) {
            result = max;
        }

        return normalizeMoney(result);
    }

    private String buildBaseSource(CampsitePricingRule rule) {
        if (rule == null) {
            return "DEFAULT";
        }

        if (rule.getTargetType() == PricingTargetType.SITE) {
            return "RULE_SITE";
        }

        return "RULE_GROUP";
    }

    private String buildBaseLabel(CampsitePricingRule rule) {
        if (rule == null) {
            return "Prix de base";
        }

        if (rule.getLabel() != null && !rule.getLabel().isBlank()) {
            return rule.getLabel();
        }

        if (rule.getTargetType() == PricingTargetType.SITE) {
            return "Règle site";
        }

        return "Règle regroupement";
    }

    private BigDecimal applyPromotionToBasePrice(BigDecimal basePrice, PricingPromotion promotion) {
        if (promotion.getPromotionType() == PromotionType.FIXED_PRICE) {
            if (promotion.getApplicationMode() == PromotionApplicationMode.OVERRIDE) {
                return promotion.getFixedPrice() != null ? promotion.getFixedPrice() : basePrice;
            }

            return promotion.getFixedPrice() != null ? promotion.getFixedPrice() : basePrice;
        }

        if (promotion.getPromotionType() == PromotionType.PERCENT_DISCOUNT) {
            BigDecimal discount = promotion.getDiscountPercent() == null
                    ? BigDecimal.ZERO
                    : promotion.getDiscountPercent();

            BigDecimal factor = BigDecimal.ONE.subtract(
                    discount.divide(ONE_HUNDRED, 4, RoundingMode.HALF_UP)
            );

            if (factor.compareTo(BigDecimal.ZERO) < 0) {
                factor = BigDecimal.ZERO;
            }

            return normalizeMoney(basePrice.multiply(factor));
        }

        return basePrice;
    }

    private void applyBuyXPayYPromotions(List<CalculatedNight> nights) {
        int index = 0;

        while (index < nights.size()) {
            CalculatedNight start = nights.get(index);

            if (start.promotion == null || start.promotion.getPromotionType() != PromotionType.BUY_X_PAY_Y) {
                index++;
                continue;
            }

            PricingPromotion promo = start.promotion;

            if (promo.getBuyNights() == null || promo.getPayNights() == null) {
                index++;
                continue;
            }

            int buyNights = promo.getBuyNights();
            int payNights = promo.getPayNights();

            if (buyNights <= 0 || payNights < 0 || payNights >= buyNights) {
                index++;
                continue;
            }

            List<CalculatedNight> segment = new ArrayList<>();
            int j = index;

            while (j < nights.size()) {
                CalculatedNight current = nights.get(j);

                if (current.promotion == null || !current.promotion.getId().equals(promo.getId())) {
                    break;
                }

                segment.add(current);
                j++;
            }

            applyBuyXPayYOnSegment(segment, promo, buyNights, payNights);

            index = j;
        }
    }

    private void applyBuyXPayYOnSegment(
            List<CalculatedNight> segment,
            PricingPromotion promo,
            int buyNights,
            int payNights
    ) {
        int freeNightsPerBlock = buyNights - payNights;

        for (int start = 0; start + buyNights <= segment.size(); start += buyNights) {
            List<CalculatedNight> block = new ArrayList<>(segment.subList(start, start + buyNights));

            block.sort(Comparator.comparing(n -> n.basePrice));

            for (int i = 0; i < freeNightsPerBlock; i++) {
                CalculatedNight freeNight = block.get(i);
                freeNight.finalPrice = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
                freeNight.source = "PROMOTION";
                freeNight.label = promo.getName() + " (nuit offerte)";
            }

            for (int i = freeNightsPerBlock; i < block.size(); i++) {
                CalculatedNight paidNight = block.get(i);
                paidNight.finalPrice = normalizeMoney(paidNight.basePrice);
                paidNight.source = "PROMOTION";
                paidNight.label = promo.getName();
            }
        }
    }

    private BigDecimal normalizeMoney(BigDecimal value) {
        if (value == null) {
            return DEFAULT_PRICE;
        }
        return value.setScale(2, RoundingMode.HALF_UP);
    }

    private static class CalculatedNight {
        private LocalDate date;
        private CampsitePricingRule baseRule;
        private BigDecimal basePrice;
        private BigDecimal finalPrice;
        private String source;
        private String label;
        private PricingPromotion promotion;
    }
}
