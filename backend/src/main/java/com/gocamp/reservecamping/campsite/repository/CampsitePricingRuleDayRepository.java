// ============================================================
// Fichier : backend/src/main/java/com/gocamp/reservecamping/campsite/repository/CampsitePricingRuleDayRepository.java
// Dernière modification : 2026-04-20
//
// Résumé :
// - Repository des jours applicables aux règles tarifaires
// ============================================================

package com.gocamp.reservecamping.campsite.repository;

import com.gocamp.reservecamping.campsite.model.CampsitePricingRuleDay;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CampsitePricingRuleDayRepository extends JpaRepository<CampsitePricingRuleDay, Long> {

    List<CampsitePricingRuleDay> findByPricingRuleId(Long pricingRuleId);

    void deleteByPricingRuleId(Long pricingRuleId);
}
