// ============================================================
// Fichier : backend/src/main/java/com/gocamp/reservecamping/campsite/service/CampsitePricingRuleService.java
// Dernière modification : 2026-04-24
//
// Résumé :
// - Service métier des règles tarifaires
// - Validation des périodes
// - Validation FIXED / DYNAMIC
// - Détection des chevauchements
// - Création / modification avec ajustement automatique
// - Suppression d’une règle
// - Support dynamicBasePrice
// - Support daysOfWeek
// - Ajout minimumNights
// ============================================================

package com.gocamp.reservecamping.campsite.service;

import com.gocamp.reservecamping.campground.model.Campground;
import com.gocamp.reservecamping.campground.repository.CampgroundRepository;
import com.gocamp.reservecamping.campsite.dto.CampsitePricingOverlapCheckResponse;
import com.gocamp.reservecamping.campsite.dto.CampsitePricingOverlapItemResponse;
import com.gocamp.reservecamping.campsite.dto.CampsitePricingRuleResponse;
import com.gocamp.reservecamping.campsite.dto.CreateCampsitePricingRuleRequest;
import com.gocamp.reservecamping.campsite.dto.UpdateCampsitePricingRuleRequest;
import com.gocamp.reservecamping.campsite.model.CampgroundSitePricingOption;
import com.gocamp.reservecamping.campsite.model.Campsite;
import com.gocamp.reservecamping.campsite.model.CampsitePricingRule;
import com.gocamp.reservecamping.campsite.model.CampsitePricingRuleDay;
import com.gocamp.reservecamping.campsite.model.PricingDayOfWeek;
import com.gocamp.reservecamping.campsite.model.PricingTargetType;
import com.gocamp.reservecamping.campsite.model.PricingType;
import com.gocamp.reservecamping.campsite.repository.CampgroundSitePricingOptionRepository;
import com.gocamp.reservecamping.campsite.repository.CampsitePricingRuleDayRepository;
import com.gocamp.reservecamping.campsite.repository.CampsitePricingRuleRepository;
import com.gocamp.reservecamping.campsite.repository.CampsiteRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional
public class CampsitePricingRuleService {

    private final CampgroundRepository campgroundRepository;
    private final CampsiteRepository campsiteRepository;
    private final CampgroundSitePricingOptionRepository pricingOptionRepository;
    private final CampsitePricingRuleRepository repository;
    private final CampsitePricingRuleDayRepository pricingRuleDayRepository;

    public CampsitePricingRuleService(
            CampgroundRepository campgroundRepository,
            CampsiteRepository campsiteRepository,
            CampgroundSitePricingOptionRepository pricingOptionRepository,
            CampsitePricingRuleRepository repository,
            CampsitePricingRuleDayRepository pricingRuleDayRepository
    ) {
        this.campgroundRepository = campgroundRepository;
        this.campsiteRepository = campsiteRepository;
        this.pricingOptionRepository = pricingOptionRepository;
        this.repository = repository;
        this.pricingRuleDayRepository = pricingRuleDayRepository;
    }

    public List<CampsitePricingRuleResponse> getByCampground(Long campgroundId) {
        return repository.findByCampgroundIdOrderByStartDateAscEndDateAsc(campgroundId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public CampsitePricingOverlapCheckResponse previewCreate(CreateCampsitePricingRuleRequest req) {
        Campground campground = validateCampground(req.campgroundId());
        PricingTargetType targetType = parseTargetType(req.targetType());
        PricingType pricingType = parsePricingType(req.pricingType());

        validateDates(req.startDate(), req.endDate());
        validatePrices(pricingType, req.fixedPrice(), req.dynamicBasePrice(), req.dynamicMinPrice(), req.dynamicMaxPrice());
        validateMinimumNights(req.minimumNights());

        ResolvedTarget target = resolveTargetForCreate(campground, targetType, req.campsiteId(), req.pricingOptionId());

        List<CampsitePricingRule> overlaps = findOverlaps(
                campground.getId(),
                targetType,
                target.campsite(),
                target.pricingOption(),
                req.startDate(),
                req.endDate(),
                null
        );

        return buildOverlapCheckResponse(overlaps);
    }

    public CampsitePricingOverlapCheckResponse previewUpdate(Long id, UpdateCampsitePricingRuleRequest req) {
        CampsitePricingRule existingRule = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Règle tarifaire introuvable."));

        PricingType pricingType = parsePricingType(req.pricingType());

        validateDates(req.startDate(), req.endDate());
        validatePrices(pricingType, req.fixedPrice(), req.dynamicBasePrice(), req.dynamicMinPrice(), req.dynamicMaxPrice());
        validateMinimumNights(req.minimumNights());

        List<CampsitePricingRule> overlaps = findOverlaps(
                existingRule.getCampground().getId(),
                existingRule.getTargetType(),
                existingRule.getCampsite(),
                existingRule.getPricingOption(),
                req.startDate(),
                req.endDate(),
                existingRule.getId()
        );

        return buildOverlapCheckResponse(overlaps);
    }

    public CampsitePricingRuleResponse create(CreateCampsitePricingRuleRequest req) {
        Campground campground = validateCampground(req.campgroundId());
        PricingTargetType targetType = parseTargetType(req.targetType());
        PricingType pricingType = parsePricingType(req.pricingType());

        validateDates(req.startDate(), req.endDate());
        validatePrices(pricingType, req.fixedPrice(), req.dynamicBasePrice(), req.dynamicMinPrice(), req.dynamicMaxPrice());
        validateMinimumNights(req.minimumNights());

        ResolvedTarget target = resolveTargetForCreate(campground, targetType, req.campsiteId(), req.pricingOptionId());

        List<CampsitePricingRule> overlaps = findOverlaps(
                campground.getId(),
                targetType,
                target.campsite(),
                target.pricingOption(),
                req.startDate(),
                req.endDate(),
                null
        );

        if (!overlaps.isEmpty() && !Boolean.TRUE.equals(req.forceAdjustOverlaps())) {
            throw new RuntimeException(buildOverlapMessage(overlaps));
        }

        if (!overlaps.isEmpty()) {
            adjustOverlappingRules(overlaps, req.startDate(), req.endDate());
        }

        CampsitePricingRule rule = new CampsitePricingRule();
        rule.setCampground(campground);
        rule.setTargetType(targetType);
        rule.setPricingType(pricingType);
        rule.setCampsite(target.campsite());
        rule.setPricingOption(target.pricingOption());
        rule.setStartDate(req.startDate());
        rule.setEndDate(req.endDate());
        rule.setMinimumNights(req.minimumNights());

        applyPricingValues(
                rule,
                pricingType,
                req.fixedPrice(),
                req.dynamicBasePrice(),
                req.dynamicMinPrice(),
                req.dynamicMaxPrice()
        );

        rule.setLabel(clean(req.label()));
        rule.setNotes(clean(req.notes()));
        rule.setActive(req.isActive() == null || req.isActive());

        repository.save(rule);
        replaceRuleDays(rule, req.daysOfWeek());

        return toResponse(rule);
    }

    public CampsitePricingRuleResponse update(Long id, UpdateCampsitePricingRuleRequest req) {
        CampsitePricingRule rule = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Règle tarifaire introuvable."));

        PricingType pricingType = parsePricingType(req.pricingType());

        validateDates(req.startDate(), req.endDate());
        validatePrices(pricingType, req.fixedPrice(), req.dynamicBasePrice(), req.dynamicMinPrice(), req.dynamicMaxPrice());
        validateMinimumNights(req.minimumNights());

        List<CampsitePricingRule> overlaps = findOverlaps(
                rule.getCampground().getId(),
                rule.getTargetType(),
                rule.getCampsite(),
                rule.getPricingOption(),
                req.startDate(),
                req.endDate(),
                rule.getId()
        );

        if (!overlaps.isEmpty() && !Boolean.TRUE.equals(req.forceAdjustOverlaps())) {
            throw new RuntimeException(buildOverlapMessage(overlaps));
        }

        if (!overlaps.isEmpty()) {
            adjustOverlappingRules(overlaps, req.startDate(), req.endDate());
        }

        rule.setPricingType(pricingType);
        rule.setStartDate(req.startDate());
        rule.setEndDate(req.endDate());
        rule.setMinimumNights(req.minimumNights());

        applyPricingValues(
                rule,
                pricingType,
                req.fixedPrice(),
                req.dynamicBasePrice(),
                req.dynamicMinPrice(),
                req.dynamicMaxPrice()
        );

        rule.setLabel(clean(req.label()));
        rule.setNotes(clean(req.notes()));
        rule.setActive(req.isActive() == null || req.isActive());

        repository.save(rule);
        replaceRuleDays(rule, req.daysOfWeek());

        return toResponse(rule);
    }

    public void delete(Long id) {
        CampsitePricingRule rule = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Règle tarifaire introuvable."));

        pricingRuleDayRepository.deleteByPricingRuleId(rule.getId());
        repository.delete(rule);
    }

    public CampsitePricingRule findBestRuleForDate(
            Campsite campsite,
            Long pricingOptionId,
            LocalDate date,
            PricingDayOfWeek dayOfWeek
    ) {
        List<CampsitePricingRule> siteRules =
                repository.findByCampgroundIdAndTargetTypeAndCampsiteIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByStartDateAscEndDateAsc(
                        campsite.getCampground().getId(),
                        PricingTargetType.SITE,
                        campsite.getId(),
                        date,
                        date
                );

        CampsitePricingRule siteRule = pickBestRule(siteRules, dayOfWeek);
        if (siteRule != null) {
            return siteRule;
        }

        if (pricingOptionId == null) {
            return null;
        }

        List<CampsitePricingRule> groupRules =
                repository.findByCampgroundIdAndTargetTypeAndPricingOptionIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByStartDateAscEndDateAsc(
                        campsite.getCampground().getId(),
                        PricingTargetType.GROUP,
                        pricingOptionId,
                        date,
                        date
                );

        return pickBestRule(groupRules, dayOfWeek);
    }

    private CampsitePricingRule pickBestRule(List<CampsitePricingRule> rules, PricingDayOfWeek dayOfWeek) {
        return rules.stream()
                .filter(CampsitePricingRule::isActive)
                .filter(rule -> matchesDay(rule, dayOfWeek))
                .max(
                        Comparator.comparing(CampsitePricingRule::getStartDate)
                                .thenComparing(CampsitePricingRule::getEndDate)
                )
                .orElse(null);
    }

    private boolean matchesDay(CampsitePricingRule rule, PricingDayOfWeek dayOfWeek) {
        List<CampsitePricingRuleDay> days = pricingRuleDayRepository.findByPricingRuleId(rule.getId());

        if (days == null || days.isEmpty()) {
            return true;
        }

        return days.stream().anyMatch(d -> d.getDayOfWeek() == dayOfWeek);
    }

    private void replaceRuleDays(CampsitePricingRule rule, List<String> daysOfWeek) {
        pricingRuleDayRepository.deleteByPricingRuleId(rule.getId());

        if (daysOfWeek == null || daysOfWeek.isEmpty()) {
            return;
        }

        for (String rawDay : daysOfWeek) {
            if (rawDay == null || rawDay.isBlank()) {
                continue;
            }

            PricingDayOfWeek day;

            try {
                day = PricingDayOfWeek.valueOf(rawDay.trim().toUpperCase());
            } catch (Exception e) {
                throw new RuntimeException("Jour invalide : " + rawDay);
            }

            CampsitePricingRuleDay item = new CampsitePricingRuleDay();
            item.setPricingRule(rule);
            item.setDayOfWeek(day);
            pricingRuleDayRepository.save(item);
        }
    }

    private List<String> getRuleDays(Long ruleId) {
        return pricingRuleDayRepository.findByPricingRuleId(ruleId)
                .stream()
                .map(day -> day.getDayOfWeek().name())
                .toList();
    }

    private Campground validateCampground(Long campgroundId) {
        if (campgroundId == null) {
            throw new RuntimeException("Le camping est requis.");
        }

        return campgroundRepository.findById(campgroundId)
                .orElseThrow(() -> new RuntimeException("Camping introuvable."));
    }

    private PricingTargetType parseTargetType(String value) {
        try {
            return PricingTargetType.valueOf(value);
        } catch (Exception e) {
            throw new RuntimeException("Type de cible invalide. Valeurs permises : SITE, GROUP.");
        }
    }

    private PricingType parsePricingType(String value) {
        try {
            return PricingType.valueOf(value);
        } catch (Exception e) {
            throw new RuntimeException("Type de tarification invalide. Valeurs permises : FIXED, DYNAMIC.");
        }
    }

    private void validateDates(LocalDate startDate, LocalDate endDate) {
        if (startDate == null) {
            throw new RuntimeException("La date de début est requise.");
        }

        if (endDate == null) {
            throw new RuntimeException("La date de fin est requise.");
        }

        if (endDate.isBefore(startDate)) {
            throw new RuntimeException("La date de fin doit être égale ou postérieure à la date de début.");
        }
    }

    private void validateMinimumNights(Integer minimumNights) {
        if (minimumNights == null) {
            return;
        }

        if (minimumNights < 1) {
            throw new RuntimeException("Le minimum de nuits doit être supérieur ou égal à 1.");
        }
    }

    private void validatePrices(
            PricingType pricingType,
            BigDecimal fixedPrice,
            BigDecimal dynamicBasePrice,
            BigDecimal dynamicMinPrice,
            BigDecimal dynamicMaxPrice
    ) {
        if (pricingType == PricingType.FIXED) {
            if (fixedPrice == null) {
                throw new RuntimeException("Le prix fixe est requis.");
            }

            if (fixedPrice.compareTo(BigDecimal.ZERO) < 0) {
                throw new RuntimeException("Le prix fixe doit être positif ou nul.");
            }
        }

        if (pricingType == PricingType.DYNAMIC) {
            if (dynamicMinPrice == null || dynamicBasePrice == null || dynamicMaxPrice == null) {
                throw new RuntimeException("Les prix min, référence et max sont requis pour une tarification dynamique.");
            }

            if (dynamicMinPrice.compareTo(BigDecimal.ZERO) < 0
                    || dynamicBasePrice.compareTo(BigDecimal.ZERO) < 0
                    || dynamicMaxPrice.compareTo(BigDecimal.ZERO) < 0) {
                throw new RuntimeException("Les prix dynamiques doivent être positifs ou nuls.");
            }

            if (dynamicBasePrice.compareTo(dynamicMinPrice) < 0) {
                throw new RuntimeException("Le prix de référence doit être supérieur ou égal au prix minimum.");
            }

            if (dynamicBasePrice.compareTo(dynamicMaxPrice) > 0) {
                throw new RuntimeException("Le prix de référence doit être inférieur ou égal au prix maximum.");
            }

            if (dynamicMaxPrice.compareTo(dynamicMinPrice) < 0) {
                throw new RuntimeException("Le prix maximum doit être supérieur ou égal au prix minimum.");
            }
        }
    }

    private ResolvedTarget resolveTargetForCreate(
            Campground campground,
            PricingTargetType targetType,
            Long campsiteId,
            Long pricingOptionId
    ) {
        Campsite campsite = null;
        CampgroundSitePricingOption pricingOption = null;

        if (targetType == PricingTargetType.SITE) {
            if (campsiteId == null) {
                throw new RuntimeException("Le site est requis pour une règle de type SITE.");
            }

            campsite = campsiteRepository.findById(campsiteId)
                    .orElseThrow(() -> new RuntimeException("Site introuvable."));

            if (!campsite.getCampground().getId().equals(campground.getId())) {
                throw new RuntimeException("Le site ne correspond pas au camping.");
            }
        } else {
            if (pricingOptionId == null) {
                throw new RuntimeException("Le regroupement est requis pour une règle de type GROUP.");
            }

            pricingOption = pricingOptionRepository
                    .findByIdAndCampgroundId(pricingOptionId, campground.getId())
                    .orElseThrow(() -> new RuntimeException("Regroupement introuvable pour ce camping."));
        }

        return new ResolvedTarget(campsite, pricingOption);
    }

    private List<CampsitePricingRule> findOverlaps(
            Long campgroundId,
            PricingTargetType targetType,
            Campsite campsite,
            CampgroundSitePricingOption pricingOption,
            LocalDate newStart,
            LocalDate newEnd,
            Long excludeRuleId
    ) {
        List<CampsitePricingRule> overlaps;

        if (targetType == PricingTargetType.SITE) {
            overlaps = repository
                    .findByCampgroundIdAndTargetTypeAndCampsiteIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByStartDateAscEndDateAsc(
                            campgroundId,
                            targetType,
                            campsite.getId(),
                            newEnd,
                            newStart
                    );
        } else {
            overlaps = repository
                    .findByCampgroundIdAndTargetTypeAndPricingOptionIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByStartDateAscEndDateAsc(
                            campgroundId,
                            targetType,
                            pricingOption.getId(),
                            newEnd,
                            newStart
                    );
        }

        if (excludeRuleId == null) {
            return overlaps;
        }

        return overlaps.stream()
                .filter(rule -> !rule.getId().equals(excludeRuleId))
                .toList();
    }

    private CampsitePricingOverlapCheckResponse buildOverlapCheckResponse(List<CampsitePricingRule> overlaps) {
        List<CampsitePricingOverlapItemResponse> items = overlaps.stream()
                .sorted(Comparator.comparing(CampsitePricingRule::getStartDate))
                .map(this::toOverlapItem)
                .toList();

        if (items.isEmpty()) {
            return new CampsitePricingOverlapCheckResponse(
                    false,
                    "Aucun chevauchement détecté.",
                    items
            );
        }

        return new CampsitePricingOverlapCheckResponse(
                true,
                buildOverlapMessage(overlaps),
                items
        );
    }

    private String buildOverlapMessage(List<CampsitePricingRule> overlaps) {
        if (overlaps.isEmpty()) {
            return "Aucun chevauchement détecté.";
        }

        return "Cette nouvelle période chevauche " + overlaps.size()
                + " règle(s) existante(s). Si vous continuez, les périodes existantes seront ajustées automatiquement.";
    }

    private void adjustOverlappingRules(
            List<CampsitePricingRule> overlaps,
            LocalDate newStart,
            LocalDate newEnd
    ) {
        List<CampsitePricingRule> rulesToCreate = new ArrayList<>();
        List<CampsitePricingRule> rulesToDelete = new ArrayList<>();

        for (CampsitePricingRule existing : overlaps) {
            LocalDate oldStart = existing.getStartDate();
            LocalDate oldEnd = existing.getEndDate();

            boolean coversEntireExisting =
                    !newStart.isAfter(oldStart) && !newEnd.isBefore(oldEnd);

            boolean overlapInMiddle =
                    oldStart.isBefore(newStart) && oldEnd.isAfter(newEnd);

            boolean overlapOnRightPart =
                    oldStart.isBefore(newStart)
                            && (oldEnd.isEqual(newStart) || oldEnd.isAfter(newStart))
                            && !oldEnd.isAfter(newEnd);

            boolean overlapOnLeftPart =
                    !oldStart.isBefore(newStart)
                            && (oldStart.isBefore(newEnd) || oldStart.isEqual(newEnd))
                            && oldEnd.isAfter(newEnd);

            if (coversEntireExisting) {
                rulesToDelete.add(existing);
                continue;
            }

            if (overlapInMiddle) {
                LocalDate originalEnd = existing.getEndDate();

                existing.setEndDate(newStart.minusDays(1));
                repository.save(existing);

                CampsitePricingRule rightPart = cloneRule(existing);
                rightPart.setStartDate(newEnd.plusDays(1));
                rightPart.setEndDate(originalEnd);

                rulesToCreate.add(rightPart);
                continue;
            }

            if (overlapOnRightPart) {
                existing.setEndDate(newStart.minusDays(1));
                repository.save(existing);
                continue;
            }

            if (overlapOnLeftPart) {
                existing.setStartDate(newEnd.plusDays(1));
                repository.save(existing);
            }
        }

        if (!rulesToDelete.isEmpty()) {
            repository.deleteAll(rulesToDelete);
        }

        for (CampsitePricingRule newRulePart : rulesToCreate) {
            repository.save(newRulePart);
        }
    }

    private CampsitePricingRule cloneRule(CampsitePricingRule source) {
        CampsitePricingRule clone = new CampsitePricingRule();
        clone.setCampground(source.getCampground());
        clone.setTargetType(source.getTargetType());
        clone.setPricingType(source.getPricingType());
        clone.setCampsite(source.getCampsite());
        clone.setPricingOption(source.getPricingOption());
        clone.setFixedPrice(source.getFixedPrice());
        clone.setDynamicBasePrice(source.getDynamicBasePrice());
        clone.setDynamicMinPrice(source.getDynamicMinPrice());
        clone.setDynamicMaxPrice(source.getDynamicMaxPrice());
        clone.setMinimumNights(source.getMinimumNights());
        clone.setLabel(source.getLabel());
        clone.setNotes(source.getNotes());
        clone.setActive(source.isActive());
        return clone;
    }

    private void applyPricingValues(
            CampsitePricingRule rule,
            PricingType pricingType,
            BigDecimal fixedPrice,
            BigDecimal dynamicBasePrice,
            BigDecimal dynamicMinPrice,
            BigDecimal dynamicMaxPrice
    ) {
        if (pricingType == PricingType.FIXED) {
            rule.setFixedPrice(fixedPrice);
            rule.setDynamicBasePrice(null);
            rule.setDynamicMinPrice(null);
            rule.setDynamicMaxPrice(null);
        } else {
            rule.setFixedPrice(null);
            rule.setDynamicBasePrice(dynamicBasePrice);
            rule.setDynamicMinPrice(dynamicMinPrice);
            rule.setDynamicMaxPrice(dynamicMaxPrice);
        }
    }

    private String clean(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }

    private CampsitePricingOverlapItemResponse toOverlapItem(CampsitePricingRule rule) {
        String targetLabel = rule.getTargetType() == PricingTargetType.SITE
                ? (rule.getCampsite() != null ? rule.getCampsite().getSiteCode() : null)
                : (rule.getPricingOption() != null ? rule.getPricingOption().getName() : null);

        return new CampsitePricingOverlapItemResponse(
                rule.getId(),
                rule.getTargetType().name(),
                targetLabel,
                rule.getPricingType().name(),
                rule.getStartDate(),
                rule.getEndDate(),
                rule.getFixedPrice(),
                rule.getDynamicBasePrice(),
                rule.getDynamicMinPrice(),
                rule.getDynamicMaxPrice(),
                rule.getMinimumNights(),
                rule.getLabel(),
                rule.isActive(),
                getRuleDays(rule.getId())
        );
    }

    private CampsitePricingRuleResponse toResponse(CampsitePricingRule rule) {
        return new CampsitePricingRuleResponse(
                rule.getId(),
                rule.getCampground().getId(),
                rule.getTargetType().name(),
                rule.getCampsite() != null ? rule.getCampsite().getId() : null,
                rule.getCampsite() != null ? rule.getCampsite().getSiteCode() : null,
                rule.getPricingOption() != null ? rule.getPricingOption().getId() : null,
                rule.getPricingOption() != null ? rule.getPricingOption().getName() : null,
                rule.getPricingType().name(),
                rule.getStartDate(),
                rule.getEndDate(),
                rule.getFixedPrice(),
                rule.getDynamicBasePrice(),
                rule.getDynamicMinPrice(),
                rule.getDynamicMaxPrice(),
                rule.getMinimumNights(),
                rule.getLabel(),
                rule.getNotes(),
                rule.isActive(),
                getRuleDays(rule.getId())
        );
    }

    private record ResolvedTarget(
            Campsite campsite,
            CampgroundSitePricingOption pricingOption
    ) {}
}