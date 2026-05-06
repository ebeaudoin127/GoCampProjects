// ============================================================
// Fichier : PricingPromotionCampsiteRepository.java
// Dernière modification : 2026-05-05
//
// Résumé :
// - Repository des associations promotion tarifaire ↔ sites
// ============================================================

package com.gocamp.reservecamping.campsite.repository;

import com.gocamp.reservecamping.campsite.model.PricingPromotionCampsite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PricingPromotionCampsiteRepository extends JpaRepository<PricingPromotionCampsite, Long> {

    List<PricingPromotionCampsite> findByPricingPromotionId(Long pricingPromotionId);

    boolean existsByPricingPromotionIdAndCampsiteId(Long pricingPromotionId, Long campsiteId);

    void deleteByPricingPromotionId(Long pricingPromotionId);
}