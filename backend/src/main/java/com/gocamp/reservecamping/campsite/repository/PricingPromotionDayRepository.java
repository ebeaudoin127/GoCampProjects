// ============================================================
// Fichier : backend/src/main/java/com/gocamp/reservecamping/campsite/repository/PricingPromotionDayRepository.java
// Dernière modification : 2026-04-20
//
// Résumé :
// - Repository des jours applicables aux promotions
// ============================================================

package com.gocamp.reservecamping.campsite.repository;

import com.gocamp.reservecamping.campsite.model.PricingPromotionDay;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PricingPromotionDayRepository extends JpaRepository<PricingPromotionDay, Long> {

    List<PricingPromotionDay> findByPricingPromotionId(Long pricingPromotionId);

    void deleteByPricingPromotionId(Long pricingPromotionId);
}
