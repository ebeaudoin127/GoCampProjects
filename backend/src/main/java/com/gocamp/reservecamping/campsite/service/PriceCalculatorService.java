// ============================================================
// Fichier : PriceCalculatorService.java
// Dernière modification : 2026-05-05
//
// Résumé :
// - Service de calcul de prix admin
// - Vérifie les indisponibilités bloquantes
// - Cherche les règles SITE puis GROUP
// - Respecte dates, minimum de nuits et jours applicables
// - Ne crée aucune réservation
// ============================================================

package com.gocamp.reservecamping.campsite.service;

import com.gocamp.reservecamping.campsite.dto.PriceCalculationLineResponse;
import com.gocamp.reservecamping.campsite.dto.PriceCalculationRequest;
import com.gocamp.reservecamping.campsite.dto.PriceCalculationResponse;
import com.gocamp.reservecamping.campsite.model.CampsitePricingAssignment;
import com.gocamp.reservecamping.campsite.model.CampsitePricingRule;
import com.gocamp.reservecamping.campsite.model.CampsitePricingRuleDay;
import com.gocamp.reservecamping.campsite.model.PricingDayOfWeek;
import com.gocamp.reservecamping.campsite.model.PricingTargetType;
import com.gocamp.reservecamping.campsite.model.PricingType;
import com.gocamp.reservecamping.campsite.repository.CampsitePricingAssignmentRepository;
import com.gocamp.reservecamping.campsite.repository.CampsitePricingRuleDayRepository;
import com.gocamp.reservecamping.campsite.repository.CampsitePricingRuleRepository;
import com.gocamp.reservecamping.campsite.repository.CampsiteUnavailabilityRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class PriceCalculatorService {

    private final CampsitePricingRuleRepository ruleRepository;
    private final CampsitePricingRuleDayRepository ruleDayRepository;
    private final CampsitePricingAssignmentRepository assignmentRepository;
    private final CampsiteUnavailabilityRepository unavailabilityRepository;

    public PriceCalculatorService(
            CampsitePricingRuleRepository ruleRepository,
            CampsitePricingRuleDayRepository ruleDayRepository,
            CampsitePricingAssignmentRepository assignmentRepository,
            CampsiteUnavailabilityRepository unavailabilityRepository
    ) {
        this.ruleRepository = ruleRepository;
        this.ruleDayRepository = ruleDayRepository;
        this.assignmentRepository = assignmentRepository;
        this.unavailabilityRepository = unavailabilityRepository;
    }

    public PriceCalculationResponse calculate(PriceCalculationRequest req) {
        validate(req);

        int nights = (int) ChronoUnit.DAYS.between(req.startDate(), req.endDate());

        boolean unavailable = unavailabilityRepository
                .existsByCampsiteIdAndIsBlockingTrueAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                        req.campsiteId(),
                        req.endDate().minusDays(1),
                        req.startDate()
                );

        if (unavailable) {
            return new PriceCalculationResponse(
                    false,
                    "Le site est indisponible pour au moins une nuit dans la période sélectionnée.",
                    nights,
                    BigDecimal.ZERO,
                    List.of()
            );
        }

        List<CampsitePricingRule> siteRules =
                ruleRepository.findByCampgroundIdAndTargetTypeAndCampsiteIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByStartDateAscEndDateAsc(
                        req.campgroundId(),
                        PricingTargetType.SITE,
                        req.campsiteId(),
                        req.endDate().minusDays(1),
                        req.startDate()
                );

        Long pricingOptionId = assignmentRepository.findByCampsiteId(req.campsiteId())
                .map(CampsitePricingAssignment::getPricingOption)
                .map(option -> option != null ? option.getId() : null)
                .orElse(null);

        List<CampsitePricingRule> groupRules = pricingOptionId == null
                ? List.of()
                : ruleRepository.findByCampgroundIdAndTargetTypeAndPricingOptionIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByStartDateAscEndDateAsc(
                req.campgroundId(),
                PricingTargetType.GROUP,
                pricingOptionId,
                req.endDate().minusDays(1),
                req.startDate()
        );

        List<PriceCalculationLineResponse> lines = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (int i = 0; i < nights; i++) {
            LocalDate nightDate = req.startDate().plusDays(i);

            CampsitePricingRule rule = findBestRule(siteRules, groupRules, nightDate, nights);

            if (rule == null) {
                return new PriceCalculationResponse(
                        false,
                        "Aucune règle tarifaire applicable trouvée pour la nuit du " + nightDate + ".",
                        nights,
                        total,
                        lines
                );
            }

            BigDecimal amount = resolveAmount(rule);

            if (amount == null) {
                return new PriceCalculationResponse(
                        false,
                        "La règle tarifaire '" + safeLabel(rule) + "' ne contient aucun prix utilisable.",
                        nights,
                        total,
                        lines
                );
            }

            lines.add(new PriceCalculationLineResponse(
                    nightDate,
                    buildLineLabel(rule),
                    amount
            ));

            total = total.add(amount);
        }

        return new PriceCalculationResponse(
                true,
                "Prix calculé avec succès.",
                nights,
                total,
                lines
        );
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
            if (!isRuleApplicable(rule, nightDate, totalNights)) {
                continue;
            }

            return rule;
        }

        return null;
    }

    private boolean isRuleApplicable(
            CampsitePricingRule rule,
            LocalDate nightDate,
            int totalNights
    ) {
        if (rule == null) {
            return false;
        }

        if (!rule.isActive()) {
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

        return isDayApplicable(rule, nightDate);
    }

    private boolean isDayApplicable(CampsitePricingRule rule, LocalDate nightDate) {
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
}