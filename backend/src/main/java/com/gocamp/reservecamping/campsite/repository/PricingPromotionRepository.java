// ============================================================
// Fichier : backend/src/main/java/com/gocamp/reservecamping/campsite/repository/PricingPromotionRepository.java
// Dernière modification : 2026-04-20
//
// Résumé :
// - Repository des promotions ponctuelles
// ============================================================

package com.gocamp.reservecamping.campsite.repository;

import com.gocamp.reservecamping.campsite.model.PricingPromotion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PricingPromotionRepository extends JpaRepository<PricingPromotion, Long> {

    List<PricingPromotion> findByCampgroundIdOrderByPriorityAscStartDateAscEndDateAsc(Long campgroundId);
}