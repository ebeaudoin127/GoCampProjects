// ============================================================
// Fichier : CampgroundPromotionService.java
// Dernière modification : 2026-05-04
// Auteur : ChatGPT
//
// Résumé :
// - Service backend des promotions marketing
// - Utilise campground_promotion
// - Validation + logique métier
// ============================================================

package com.gocamp.reservecamping.campground;

import com.gocamp.reservecamping.campground.dto.*;
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

    // ============================================================
    // LISTE
    // ============================================================
    public List<CampgroundPromotionResponse> getByCampground(Long campgroundId) {
        return repo.findByCampgroundIdOrderByStartDateDesc(campgroundId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // ============================================================
    // CREATE
    // ============================================================
    @Transactional
    public CampgroundPromotionResponse create(
            Long campgroundId,
            CreateCampgroundPromotionRequest req
    ) {
        Campground campground = campgroundRepo.findById(campgroundId)
                .orElseThrow(() -> new RuntimeException("Camping introuvable."));

        validate(req.title(), req.startDate(), req.endDate(), req.discountType(), req.discountValue());

        CampgroundPromotion p = new CampgroundPromotion();
        p.setCampground(campground);

        apply(p, req);

        return toResponse(repo.save(p));
    }

    // ============================================================
    // UPDATE
    // ============================================================
    @Transactional
    public CampgroundPromotionResponse update(
            Long campgroundId,
            Long promotionId,
            UpdateCampgroundPromotionRequest req
    ) {
        CampgroundPromotion p = repo.findById(promotionId)
                .orElseThrow(() -> new RuntimeException("Promotion introuvable."));

        if (!p.getCampground().getId().equals(campgroundId)) {
            throw new RuntimeException("Promotion invalide pour ce camping.");
        }

        validate(req.title(), req.startDate(), req.endDate(), req.discountType(), req.discountValue());

        apply(p, req);

        return toResponse(repo.save(p));
    }

    // ============================================================
    // DELETE
    // ============================================================
    @Transactional
    public void delete(Long campgroundId, Long promotionId) {
        CampgroundPromotion p = repo.findById(promotionId)
                .orElseThrow(() -> new RuntimeException("Promotion introuvable."));

        if (!p.getCampground().getId().equals(campgroundId)) {
            throw new RuntimeException("Promotion invalide pour ce camping.");
        }

        repo.delete(p);
    }

    // ============================================================
    // APPLY DTO
    // ============================================================
    private void apply(CampgroundPromotion p, CreateCampgroundPromotionRequest req) {
        p.setTitle(req.title());
        p.setDescription(req.description());
        p.setPromoCode(req.promoCode() != null ? req.promoCode().toUpperCase() : null);
        p.setStartDate(req.startDate());
        p.setEndDate(req.endDate());
        p.setDiscountType(req.discountType());
        p.setDiscountValue(req.discountValue());
        p.setConditionsText(req.conditionsText());
        p.setIsActive(req.isActive() != null ? req.isActive() : true);
    }

    private void apply(CampgroundPromotion p, UpdateCampgroundPromotionRequest req) {
        p.setTitle(req.title());
        p.setDescription(req.description());
        p.setPromoCode(req.promoCode() != null ? req.promoCode().toUpperCase() : null);
        p.setStartDate(req.startDate());
        p.setEndDate(req.endDate());
        p.setDiscountType(req.discountType());
        p.setDiscountValue(req.discountValue());
        p.setConditionsText(req.conditionsText());
        p.setIsActive(req.isActive() != null ? req.isActive() : true);
    }

    // ============================================================
    // VALIDATION
    // ============================================================
    private void validate(
            String title,
            LocalDate startDate,
            LocalDate endDate,
            String discountType,
            java.math.BigDecimal discountValue
    ) {
        if (title == null || title.isBlank()) {
            throw new RuntimeException("Titre requis.");
        }

        if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
            throw new RuntimeException("Date invalide.");
        }

        if (discountValue != null && discountValue.compareTo(java.math.BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Valeur invalide.");
        }

        if ("PERCENT".equals(discountType)
                && discountValue != null
                && discountValue.compareTo(java.math.BigDecimal.valueOf(100)) > 0) {
            throw new RuntimeException("Max 100%.");
        }
    }

    // ============================================================
    // MAPPER
    // ============================================================
    private CampgroundPromotionResponse toResponse(CampgroundPromotion p) {
        return new CampgroundPromotionResponse(
                p.getId(),
                p.getTitle(),
                p.getDescription(),
                p.getPromoCode(),
                p.getStartDate(),
                p.getEndDate(),
                p.getDiscountType(),
                p.getDiscountValue(),
                p.getConditionsText(),
                p.getIsActive()
        );
    }
}