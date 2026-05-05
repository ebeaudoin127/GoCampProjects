
// ============================================================
// Fichier : CampgroundPromotionService.java
// Dernière modification : 2026-05-04
// Auteur : ChatGPT
//
// Résumé :
// - Service backend des promotions marketing d’un camping
// - Utilise la table campground_promotion
// - Ne touche pas au moteur pricing_promotion
// ============================================================

package com.gocamp.reservecamping.campground;

import com.gocamp.reservecamping.campground.dto.CampgroundPromotionResponse;
import com.gocamp.reservecamping.campground.dto.CreateCampgroundPromotionRequest;
import com.gocamp.reservecamping.campground.dto.UpdateCampgroundPromotionRequest;
import com.gocamp.reservecamping.campground.model.Campground;
import com.gocamp.reservecamping.campground.model.CampgroundPromotion;
import com.gocamp.reservecamping.campground.repository.CampgroundPromotionRepository;
import com.gocamp.reservecamping.campground.repository.CampgroundRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class CampgroundPromotionService {

    private final CampgroundPromotionRepository repo;
    private final CampgroundRepository campgroundRepo;

    public CampgroundPromotionService(
            CampgroundPromotionRepository repo,
            CampgroundRepository campgroundRepo
    ) {
        this.repo = repo;
        this.campgroundRepo = campgroundRepo;
    }

    public List<CampgroundPromotionResponse> getByCampground(Long campgroundId) {
        return repo.findByCampgroundIdOrderByStartDateAscEndDateAsc(campgroundId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public CampgroundPromotionResponse create(
            Long campgroundId,
            CreateCampgroundPromotionRequest req
    ) {
        Campground campground = campgroundRepo.findById(campgroundId)
                .orElseThrow(() -> new RuntimeException("Camping introuvable."));

        validate(req.title(), req.startDate(), req.endDate(), req.discountType(), req.discountValue());

        CampgroundPromotion promotion = new CampgroundPromotion();
        promotion.setCampground(campground);
        applyRequestToEntity(
                promotion,
                req.title(),
                req.description(),
                req.promoCode(),
                req.startDate(),
                req.endDate(),
                req.discountType(),
                req.discountValue(),
                req.conditionsText(),
                req.isActive()
        );

        return toResponse(repo.save(promotion));
    }

    @Transactional
    public CampgroundPromotionResponse update(
            Long campgroundId,
            Long promotionId,
            UpdateCampgroundPromotionRequest req
    ) {
        CampgroundPromotion promotion = repo.findById(promotionId)
                .orElseThrow(() -> new RuntimeException("Promotion introuvable."));

        if (promotion.getCampground() == null || !promotion.getCampground().getId().equals(campgroundId)) {
            throw new RuntimeException("Cette promotion n'appartient pas à ce camping.");
        }

        validate(req.title(), req.startDate(), req.endDate(), req.discountType(), req.discountValue());

        applyRequestToEntity(
                promotion,
                req.title(),
                req.description(),
                req.promoCode(),
                req.startDate(),
                req.endDate(),
                req.discountType(),
                req.discountValue(),
                req.conditionsText(),
                req.isActive()
        );

        return toResponse(repo.save(promotion));
    }

    @Transactional
    public void delete(Long campgroundId, Long promotionId) {
        CampgroundPromotion promotion = repo.findById(promotionId)
                .orElseThrow(() -> new RuntimeException("Promotion introuvable."));

        if (promotion.getCampground() == null || !promotion.getCampground().getId().equals(campgroundId)) {
            throw new RuntimeException("Cette promotion n'appartient pas à ce camping.");
        }

        repo.delete(promotion);
    }

    private void validate(
            String title,
            LocalDate startDate,
            LocalDate endDate,
            String discountType,
            java.math.BigDecimal discountValue
    ) {
        if (title == null || title.trim().isEmpty()) {
            throw new RuntimeException("Le titre de la promotion est obligatoire.");
        }

        if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
            throw new RuntimeException("La date de fin doit être après ou égale à la date de début.");
        }

        if (discountType != null && !discountType.isBlank()) {
            List<String> allowedTypes = List.of("PERCENT", "AMOUNT", "FIXED_PRICE", "NIGHTS_FOR_PRICE", "OTHER");

            if (!allowedTypes.contains(discountType)) {
                throw new RuntimeException("Type de promotion invalide.");
            }
        }

        if (discountValue != null && discountValue.compareTo(java.math.BigDecimal.ZERO) < 0) {
            throw new RuntimeException("La valeur de la promotion ne peut pas être négative.");
        }

        if ("PERCENT".equals(discountType)
                && discountValue != null
                && discountValue.compareTo(java.math.BigDecimal.valueOf(100)) > 0) {
            throw new RuntimeException("Un rabais en pourcentage ne peut pas dépasser 100%.");
        }
    }

    private void applyRequestToEntity(
            CampgroundPromotion promotion,
            String title,
            String description,
            String promoCode,
            LocalDate startDate,
            LocalDate endDate,
            String discountType,
            java.math.BigDecimal discountValue,
            String conditionsText,
            Boolean isActive
    ) {
        promotion.setTitle(title.trim());
        promotion.setDescription(trimToNull(description));
        promotion.setPromoCode(normalizePromoCode(promoCode));
        promotion.setStartDate(startDate);
        promotion.setEndDate(endDate);
        promotion.setDiscountType(trimToNull(discountType));
        promotion.setDiscountValue(discountValue);
        promotion.setConditionsText(trimToNull(conditionsText));
        promotion.setIsActive(isActive != null ? isActive : true);
    }

    private CampgroundPromotionResponse toResponse(CampgroundPromotion promotion) {
        return new CampgroundPromotionResponse(
                promotion.getId(),
                promotion.getTitle(),
                promotion.getDescription(),
                promotion.getPromoCode(),
                promotion.getStartDate(),
                promotion.getEndDate(),
                promotion.getDiscountType(),
                promotion.getDiscountValue(),
                promotion.getConditionsText(),
                promotion.getIsActive()
        );
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }

        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private String normalizePromoCode(String value) {
        String trimmed = trimToNull(value);
        return trimmed == null ? null : trimmed.toUpperCase();
    }
}