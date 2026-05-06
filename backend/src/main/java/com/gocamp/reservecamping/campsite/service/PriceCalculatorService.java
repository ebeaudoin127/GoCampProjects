// ============================================================
// Fichier : PriceCalculatorService.java
// Dernière modification : 2026-05-05
// Auteur : ChatGPT
//
// Résumé :
// - Calculateur de prix admin nuit par nuit
// - Vérifie les indisponibilités bloquantes
// - Cherche les règles SITE puis GROUP pour chaque nuit
// - Respecte dates, minimum de nuits et jours applicables
// - Applique les promotions dynamiques compatibles
// - Supporte code promo, early booking, last minute,
//   jour d’arrivée obligatoire, priorité et cumul
// ============================================================

package com.gocamp.reservecamping.campsite.service;

import com.gocamp.reservecamping.campsite.dto.AppliedPromotionResponse;
import com.gocamp.reservecamping.campsite.dto.PriceCalculationLineResponse;
import com.gocamp.reservecamping.campsite.dto.PriceCalculationRequest;
import com.gocamp.reservecamping.campsite.dto.PriceCalculationResponse;
import com.gocamp.reservecamping.campsite.dto.PriceCalculationUnavailabilityResponse;
import com.gocamp.reservecamping.campsite.model.CampsitePricingAssignment;
import com.gocamp.reservecamping.campsite.model.CampsitePricingRule;
import com.gocamp.reservecamping.campsite.model.CampsitePricingRuleDay;
import com.gocamp.reservecamping.campsite.model.CampsiteUnavailability;
import com.gocamp.reservecamping.campsite.model.PricingDayOfWeek;
import com.gocamp.reservecamping.campsite.model.PricingPromotion;
import com.gocamp.reservecamping.campsite.model.PricingPromotionDay;
import com.gocamp.reservecamping.campsite.model.PricingTargetType;
import com.gocamp.reservecamping.campsite.model.PricingType;
import com.gocamp.reservecamping.campsite.model.PromotionType;
import com.gocamp.reservecamping.campsite.repository.CampsitePricingAssignmentRepository;
import com.gocamp.reservecamping.campsite.repository.CampsitePricingRuleDayRepository;
import com.gocamp.reservecamping.campsite.repository.CampsitePricingRuleRepository;
import com.gocamp.reservecamping.campsite.repository.CampsiteUnavailabilityRepository;
import com.gocamp.reservecamping.campsite.repository.PricingPromotionCampsiteRepository;
import com.gocamp.reservecamping.campsite.repository.PricingPromotionDayRepository;
import com.gocamp.reservecamping.campsite.repository.PricingPromotionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class PriceCalculatorService {

    private final CampsitePricingRuleRepository ruleRepository;
    private final CampsitePricingRuleDayRepository ruleDayRepository;
    private final CampsitePricingAssignmentRepository assignmentRepository;
    private final CampsiteUnavailabilityRepository unavailabilityRepository;
    private final PricingPromotionRepository promotionRepository;
    private final PricingPromotionDayRepository promotionDayRepository;
    private final PricingPromotionCampsiteRepository promotionCampsiteRepository;

    public PriceCalculatorService(
            CampsitePricingRuleRepository ruleRepository,
            CampsitePricingRuleDayRepository ruleDayRepository,
            CampsitePricingAssignmentRepository assignmentRepository,
            CampsiteUnavailabilityRepository unavailabilityRepository,
            PricingPromotionRepository promotionRepository,
            PricingPromotionDayRepository promotionDayRepository,
            PricingPromotionCampsiteRepository promotionCampsiteRepository
    ) {
        this.ruleRepository = ruleRepository;
        this.ruleDayRepository = ruleDayRepository;
        this.assignmentRepository = assignmentRepository;
        this.unavailabilityRepository = unavailabilityRepository;
        this.promotionRepository = promotionRepository;
        this.promotionDayRepository = promotionDayRepository;
        this.promotionCampsiteRepository = promotionCampsiteRepository;
    }

    public PriceCalculationResponse calculate(PriceCalculationRequest req) {
        validate(req);

        int nights = (int) ChronoUnit.DAYS.between(req.startDate(), req.endDate());
        LocalDate lastNightDate = req.endDate().minusDays(1);

        List<CampsiteUnavailability> blockingUnavailabilities =
                unavailabilityRepository
                        .findByCampsiteIdAndIsBlockingTrueAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByStartDateAscEndDateAsc(
                                req.campsiteId(),
                                lastNightDate,
                                req.startDate()
                        );

        if (blockingUnavailabilities != null && !blockingUnavailabilities.isEmpty()) {
            return new PriceCalculationResponse(
                    false,
                    buildUnavailableMessage(blockingUnavailabilities),
                    nights,
                    money(BigDecimal.ZERO),
                    money(BigDecimal.ZERO),
                    money(BigDecimal.ZERO),
                    List.of(),
                    List.of(),
                    blockingUnavailabilities.stream().map(this::mapUnavailability).toList()
            );
        }

        Long pricingOptionId = assignmentRepository.findByCampsiteId(req.campsiteId())
                .map(CampsitePricingAssignment::getPricingOption)
                .map(option -> option != null ? option.getId() : null)
                .orElse(null);

        List<CampsitePricingRule> siteRules =
                ruleRepository.findByCampgroundIdAndTargetTypeAndCampsiteIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByStartDateAscEndDateAsc(
                        req.campgroundId(),
                        PricingTargetType.SITE,
                        req.campsiteId(),
                        lastNightDate,
                        req.startDate()
                );

        List<CampsitePricingRule> groupRules = pricingOptionId == null
                ? List.of()
                : ruleRepository.findByCampgroundIdAndTargetTypeAndPricingOptionIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByStartDateAscEndDateAsc(
                req.campgroundId(),
                PricingTargetType.GROUP,
                pricingOptionId,
                lastNightDate,
                req.startDate()
        );

        List<PriceCalculationLineResponse> lines = new ArrayList<>();
        BigDecimal baseTotal = BigDecimal.ZERO;

        for (int i = 0; i < nights; i++) {
            LocalDate nightDate = req.startDate().plusDays(i);

            CampsitePricingRule rule = findBestRule(siteRules, groupRules, nightDate, nights);

            if (rule == null) {
                return new PriceCalculationResponse(
                        false,
                        "Aucune règle tarifaire applicable trouvée pour la nuit du " + nightDate + ".",
                        nights,
                        money(baseTotal),
                        money(BigDecimal.ZERO),
                        money(baseTotal),
                        lines,
                        List.of(),
                        List.of()
                );
            }

            BigDecimal amount = resolveAmount(rule);

            if (amount == null) {
                return new PriceCalculationResponse(
                        false,
                        "La règle tarifaire '" + safeLabel(rule) + "' ne contient aucun prix utilisable.",
                        nights,
                        money(baseTotal),
                        money(BigDecimal.ZERO),
                        money(baseTotal),
                        lines,
                        List.of(),
                        List.of()
                );
            }

            BigDecimal amountMoney = money(amount);

            lines.add(new PriceCalculationLineResponse(
                    nightDate,
                    buildLineLabel(rule),
                    amountMoney
            ));

            baseTotal = baseTotal.add(amountMoney);
        }

        baseTotal = money(baseTotal);

        PromotionCalculationResult promotionResult = applyPromotions(
                req,
                nights,
                pricingOptionId,
                lines,
                baseTotal,
                lastNightDate
        );

        BigDecimal finalTotal = baseTotal.subtract(promotionResult.totalDiscount);

        if (finalTotal.compareTo(BigDecimal.ZERO) < 0) {
            finalTotal = BigDecimal.ZERO;
        }

        return new PriceCalculationResponse(
                true,
                promotionResult.appliedPromotions.isEmpty()
                        ? "Prix calculé avec succès. Aucune promotion applicable."
                        : "Prix calculé avec succès. Promotions appliquées.",
                nights,
                money(baseTotal),
                money(promotionResult.totalDiscount),
                money(finalTotal),
                lines,
                promotionResult.appliedPromotions,
                List.of()
        );
    }

    private PromotionCalculationResult applyPromotions(
            PriceCalculationRequest req,
            int nights,
            Long pricingOptionId,
            List<PriceCalculationLineResponse> lines,
            BigDecimal baseTotal,
            LocalDate lastNightDate
    ) {
        List<PricingPromotion> promotions =
                promotionRepository.findByCampgroundIdAndIsActiveTrueAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByPriorityAscStartDateAscEndDateAsc(
                        req.campgroundId(),
                        lastNightDate,
                        req.startDate()
                );

        List<AppliedPromotionResponse> applied = new ArrayList<>();
        BigDecimal totalDiscount = BigDecimal.ZERO;

        for (PricingPromotion promotion : promotions) {
            if (!isPromotionTargetApplicable(promotion, req.campsiteId(), pricingOptionId)) {
                continue;
            }

            if (!isPromotionNightCountApplicable(promotion, nights)) {
                continue;
            }

            if (!isPromotionMarketingApplicable(promotion, req)) {
                continue;
            }

            List<PriceCalculationLineResponse> eligibleLines =
                    findEligibleLinesForPromotion(promotion, lines);

            if (eligibleLines.isEmpty()) {
                continue;
            }

            BigDecimal currentRemaining = baseTotal.subtract(totalDiscount);

            if (currentRemaining.compareTo(BigDecimal.ZERO) <= 0) {
                break;
            }

            BigDecimal discount = calculatePromotionDiscount(
                    promotion,
                    eligibleLines,
                    currentRemaining,
                    req
            );

            if (discount.compareTo(BigDecimal.ZERO) <= 0) {
                continue;
            }

            discount = money(discount.min(currentRemaining));
            totalDiscount = money(totalDiscount.add(discount));

            applied.add(new AppliedPromotionResponse(
                    promotion.getId(),
                    promotion.getName(),
                    promotion.getPromotionType() != null ? promotion.getPromotionType().name() : "UNKNOWN",
                    discount,
                    buildPromotionDescription(promotion)
            ));

            if (!promotion.isCombinable()) {
                break;
            }
        }

        return new PromotionCalculationResult(money(totalDiscount), applied);
    }

    private BigDecimal calculatePromotionDiscount(
            PricingPromotion promotion,
            List<PriceCalculationLineResponse> eligibleLines,
            BigDecimal currentRemaining,
            PriceCalculationRequest req
    ) {
        PromotionType type = promotion.getPromotionType();

        if (type == PromotionType.PERCENT_DISCOUNT && promotion.getDiscountPercent() != null) {
            BigDecimal eligibleSubtotal = subtotal(eligibleLines);

            return eligibleSubtotal
                    .multiply(promotion.getDiscountPercent())
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        }

        if (type == PromotionType.AMOUNT_DISCOUNT && promotion.getDiscountAmount() != null) {
            return promotion.getDiscountAmount();
        }

        if (type == PromotionType.FIXED_PRICE && promotion.getFixedPrice() != null) {
            BigDecimal discount = BigDecimal.ZERO;

            for (PriceCalculationLineResponse line : eligibleLines) {
                BigDecimal nightlyDiscount = line.amount().subtract(promotion.getFixedPrice());

                if (nightlyDiscount.compareTo(BigDecimal.ZERO) > 0) {
                    discount = discount.add(nightlyDiscount);
                }
            }

            return discount;
        }

        if (type == PromotionType.BUY_X_PAY_Y
                && promotion.getBuyNights() != null
                && promotion.getPayNights() != null
                && promotion.getBuyNights() > promotion.getPayNights()) {
            int groups = eligibleLines.size() / promotion.getBuyNights();
            int freeNights = groups * (promotion.getBuyNights() - promotion.getPayNights());

            if (freeNights <= 0) {
                return BigDecimal.ZERO;
            }

            return eligibleLines.stream()
                    .sorted(Comparator.comparing(PriceCalculationLineResponse::amount))
                    .limit(freeNights)
                    .map(PriceCalculationLineResponse::amount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        if (type == PromotionType.X_NIGHTS_FOR_AMOUNT
                && promotion.getPackageNights() != null
                && promotion.getPackagePrice() != null
                && promotion.getPackageNights() > 0) {
            int packages = eligibleLines.size() / promotion.getPackageNights();

            if (packages <= 0) {
                return BigDecimal.ZERO;
            }

            int coveredNights = packages * promotion.getPackageNights();

            BigDecimal baseCovered = eligibleLines.stream()
                    .sorted(Comparator.comparing(PriceCalculationLineResponse::amount).reversed())
                    .limit(coveredNights)
                    .map(PriceCalculationLineResponse::amount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal packageTotal = promotion.getPackagePrice().multiply(BigDecimal.valueOf(packages));
            BigDecimal discount = baseCovered.subtract(packageTotal);

            return discount.compareTo(BigDecimal.ZERO) > 0 ? discount : BigDecimal.ZERO;
        }

        if (type == PromotionType.CONSECUTIVE_WEEKENDS
                && promotion.getRequiredConsecutiveWeekends() != null
                && promotion.getPackagePrice() != null) {
            return calculateConsecutiveWeekendsDiscount(promotion, eligibleLines, req);
        }

        if (type == PromotionType.PACKAGE && promotion.getPackagePrice() != null) {
            BigDecimal eligibleSubtotal = subtotal(eligibleLines);
            BigDecimal discount = eligibleSubtotal.subtract(promotion.getPackagePrice());

            return discount.compareTo(BigDecimal.ZERO) > 0 ? discount : BigDecimal.ZERO;
        }

        return BigDecimal.ZERO;
    }

    private BigDecimal calculateConsecutiveWeekendsDiscount(
            PricingPromotion promotion,
            List<PriceCalculationLineResponse> eligibleLines,
            PriceCalculationRequest req
    ) {
        int requiredWeekends = promotion.getRequiredConsecutiveWeekends();

        if (requiredWeekends <= 0) {
            return BigDecimal.ZERO;
        }

        if (req.startDate().getDayOfWeek() != DayOfWeek.FRIDAY) {
            return BigDecimal.ZERO;
        }

        int expectedNights = requiredWeekends * 2;

        if (eligibleLines.size() != expectedNights) {
            return BigDecimal.ZERO;
        }

        LocalDate expectedStart = req.startDate();

        for (int i = 0; i < requiredWeekends; i++) {
            LocalDate friday = expectedStart.plusWeeks(i);
            LocalDate saturday = friday.plusDays(1);

            boolean hasFriday = eligibleLines.stream()
                    .anyMatch(line -> line.date().equals(friday));

            boolean hasSaturday = eligibleLines.stream()
                    .anyMatch(line -> line.date().equals(saturday));

            if (!hasFriday || !hasSaturday) {
                return BigDecimal.ZERO;
            }
        }

        BigDecimal eligibleSubtotal = subtotal(eligibleLines);
        BigDecimal discount = eligibleSubtotal.subtract(promotion.getPackagePrice());

        return discount.compareTo(BigDecimal.ZERO) > 0 ? discount : BigDecimal.ZERO;
    }

    private boolean isPromotionTargetApplicable(
            PricingPromotion promotion,
            Long campsiteId,
            Long pricingOptionId
    ) {
        if (promotion.getTargetType() == PricingTargetType.ALL_CAMPGROUND) {
            return true;
        }

        if (promotion.getTargetType() == PricingTargetType.SITE) {
            return promotion.getCampsite() != null
                    && promotion.getCampsite().getId().equals(campsiteId);
        }

        if (promotion.getTargetType() == PricingTargetType.GROUP) {
            return pricingOptionId != null
                    && promotion.getPricingOption() != null
                    && promotion.getPricingOption().getId().equals(pricingOptionId);
        }

        if (promotion.getTargetType() == PricingTargetType.MULTI_CAMPSITE) {
            return promotionCampsiteRepository.existsByPricingPromotionIdAndCampsiteId(
                    promotion.getId(),
                    campsiteId
            );
        }

        return false;
    }

    private boolean isPromotionNightCountApplicable(PricingPromotion promotion, int nights) {
        if (promotion.getMinNights() != null && nights < promotion.getMinNights()) {
            return false;
        }

        if (promotion.getMaxNights() != null && nights > promotion.getMaxNights()) {
            return false;
        }

        return true;
    }

    private boolean isPromotionMarketingApplicable(
            PricingPromotion promotion,
            PriceCalculationRequest req
    ) {
        if (promotion.isRequiresPromoCode()) {
            if (req.promoCode() == null || req.promoCode().isBlank()) {
                return false;
            }

            if (promotion.getPromoCode() == null || promotion.getPromoCode().isBlank()) {
                return false;
            }

            if (!promotion.getPromoCode().trim().equalsIgnoreCase(req.promoCode().trim())) {
                return false;
            }
        }

        if (promotion.getBookingBeforeDate() != null) {
            if (LocalDate.now().isAfter(promotion.getBookingBeforeDate())) {
                return false;
            }
        }

        if (promotion.getArrivalWithinDays() != null) {
            long daysUntilArrival = ChronoUnit.DAYS.between(LocalDate.now(), req.startDate());

            if (daysUntilArrival < 0 || daysUntilArrival > promotion.getArrivalWithinDays()) {
                return false;
            }
        }

        if (promotion.getRequiredArrivalDay() != null) {
            PricingDayOfWeek arrivalDay = toPricingDayOfWeek(req.startDate().getDayOfWeek());

            if (arrivalDay != promotion.getRequiredArrivalDay()) {
                return false;
            }
        }

        if (promotion.getPromotionType() == PromotionType.CONSECUTIVE_WEEKENDS) {
            if (req.startDate().getDayOfWeek() != DayOfWeek.FRIDAY) {
                return false;
            }

            if (req.endDate().getDayOfWeek() != DayOfWeek.SUNDAY) {
                return false;
            }
        }

        return true;
    }

    private List<PriceCalculationLineResponse> findEligibleLinesForPromotion(
            PricingPromotion promotion,
            List<PriceCalculationLineResponse> lines
    ) {
        return lines.stream()
                .filter(line -> !line.date().isBefore(promotion.getStartDate()))
                .filter(line -> !line.date().isAfter(promotion.getEndDate()))
                .filter(line -> isPromotionDayApplicable(promotion, line.date()))
                .toList();
    }

    private boolean isPromotionDayApplicable(PricingPromotion promotion, LocalDate date) {
        List<PricingPromotionDay> days = promotionDayRepository.findByPricingPromotionId(promotion.getId());

        if (days == null || days.isEmpty()) {
            return true;
        }

        PricingDayOfWeek requestedDay = toPricingDayOfWeek(date.getDayOfWeek());

        return days.stream()
                .map(PricingPromotionDay::getDayOfWeek)
                .anyMatch(day -> day == requestedDay);
    }

    private void validate(PriceCalculationRequest req) {
        if (req == null) {
            throw new RuntimeException("Requête obligatoire.");
        }

        if (req.campgroundId() == null) {
            throw new RuntimeException("Le camping est obligatoire.");
        }

        if (req.campsiteId() == null) {
            throw new RuntimeException("Le site est obligatoire.");
        }

        if (req.startDate() == null || req.endDate() == null) {
            throw new RuntimeException("Les dates sont obligatoires.");
        }

        if (!req.endDate().isAfter(req.startDate())) {
            throw new RuntimeException("La date de départ doit être après la date d’arrivée.");
        }
    }

    private CampsitePricingRule findBestRule(
            List<CampsitePricingRule> siteRules,
            List<CampsitePricingRule> groupRules,
            LocalDate nightDate,
            int totalNights
    ) {
        CampsitePricingRule siteRule = findMatchingRule(siteRules, nightDate, totalNights);

        if (siteRule != null) {
            return siteRule;
        }

        return findMatchingRule(groupRules, nightDate, totalNights);
    }

    private CampsitePricingRule findMatchingRule(
            List<CampsitePricingRule> rules,
            LocalDate nightDate,
            int totalNights
    ) {
        if (rules == null || rules.isEmpty()) {
            return null;
        }

        for (CampsitePricingRule rule : rules) {
            if (isRuleApplicable(rule, nightDate, totalNights)) {
                return rule;
            }
        }

        return null;
    }

    private boolean isRuleApplicable(
            CampsitePricingRule rule,
            LocalDate nightDate,
            int totalNights
    ) {
        if (rule == null || !rule.isActive()) {
            return false;
        }

        if (rule.getStartDate() != null && nightDate.isBefore(rule.getStartDate())) {
            return false;
        }

        if (rule.getEndDate() != null && nightDate.isAfter(rule.getEndDate())) {
            return false;
        }

        if (rule.getMinimumNights() != null && totalNights < rule.getMinimumNights()) {
            return false;
        }

        return isRuleDayApplicable(rule, nightDate);
    }

    private boolean isRuleDayApplicable(CampsitePricingRule rule, LocalDate nightDate) {
        List<CampsitePricingRuleDay> days = ruleDayRepository.findByPricingRuleId(rule.getId());

        if (days == null || days.isEmpty()) {
            return true;
        }

        PricingDayOfWeek requestedDay = toPricingDayOfWeek(nightDate.getDayOfWeek());

        return days.stream()
                .map(CampsitePricingRuleDay::getDayOfWeek)
                .anyMatch(day -> day == requestedDay);
    }

    private PricingDayOfWeek toPricingDayOfWeek(DayOfWeek dayOfWeek) {
        return PricingDayOfWeek.valueOf(dayOfWeek.name());
    }

    private BigDecimal resolveAmount(CampsitePricingRule rule) {
        if (rule.getPricingType() == PricingType.FIXED) {
            return rule.getFixedPrice();
        }

        if (rule.getPricingType() == PricingType.DYNAMIC) {
            return rule.getDynamicBasePrice();
        }

        if (rule.getFixedPrice() != null) {
            return rule.getFixedPrice();
        }

        return rule.getDynamicBasePrice();
    }

    private BigDecimal subtotal(List<PriceCalculationLineResponse> lines) {
        return lines.stream()
                .map(PriceCalculationLineResponse::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private String buildLineLabel(CampsitePricingRule rule) {
        String label = safeLabel(rule);
        String target = rule.getTargetType() != null ? rule.getTargetType().name() : "UNKNOWN";
        String type = rule.getPricingType() != null ? rule.getPricingType().name() : "UNKNOWN";

        return label + " [" + target + " / " + type + "]";
    }

    private String safeLabel(CampsitePricingRule rule) {
        if (rule.getLabel() != null && !rule.getLabel().isBlank()) {
            return rule.getLabel();
        }

        return "Règle tarifaire #" + rule.getId();
    }

    private String buildPromotionDescription(PricingPromotion promotion) {
        return "Priorité "
                + promotion.getPriority()
                + (promotion.isCombinable() ? " · cumulable" : " · non cumulable");
    }

    private PriceCalculationUnavailabilityResponse mapUnavailability(CampsiteUnavailability unavailability) {
        return new PriceCalculationUnavailabilityResponse(
                unavailability.getId(),
                unavailability.getStartDate(),
                unavailability.getEndDate(),
                unavailability.getReason(),
                unavailability.getNotes(),
                unavailability.isBlocking()
        );
    }

    private String buildUnavailableMessage(List<CampsiteUnavailability> unavailabilities) {
        if (unavailabilities == null || unavailabilities.isEmpty()) {
            return "Le site est indisponible pour la période sélectionnée.";
        }

        CampsiteUnavailability first = unavailabilities.get(0);

        String reason = first.getReason() != null && !first.getReason().isBlank()
                ? " Raison : " + first.getReason() + "."
                : "";

        if (unavailabilities.size() == 1) {
            return "Le site est indisponible du "
                    + first.getStartDate()
                    + " au "
                    + first.getEndDate()
                    + "."
                    + reason;
        }

        return "Le site contient "
                + unavailabilities.size()
                + " périodes d’indisponibilité qui chevauchent les dates sélectionnées.";
    }

    private BigDecimal money(BigDecimal value) {
        if (value == null) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        return value.setScale(2, RoundingMode.HALF_UP);
    }

    private record PromotionCalculationResult(
            BigDecimal totalDiscount,
            List<AppliedPromotionResponse> appliedPromotions
    ) {}
}