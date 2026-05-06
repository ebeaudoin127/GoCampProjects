// ============================================================
// Fichier : backend/src/main/java/com/gocamp/reservecamping/campsite/repository/PricingPromotionRepository.java
// Dernière modification : 2026-05-05
//
// Résumé :
// - Repository des promotions tarifaires dynamiques
// - Permet de lister les promotions par camping
// - Permet de trouver les promotions actives qui chevauchent une période
// ============================================================

package com.gocamp.reservecamping.campsite.repository;

import com.gocamp.reservecamping.campsite.model.PricingPromotion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PricingPromotionRepository extends JpaRepository<PricingPromotion, Long> {

    List<PricingPromotion> findByCampgroundIdOrderByPriorityAscStartDateAscEndDateAsc(Long campgroundId);

    List<PricingPromotion> findByCampgroundIdAndIsActiveTrueAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByPriorityAscStartDateAscEndDateAsc(
            Long campgroundId,
            LocalDate requestedEndDate,
            LocalDate requestedStartDate
    );
}