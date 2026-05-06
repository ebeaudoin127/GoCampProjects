// ============================================================
// Fichier : backend/src/main/java/com/gocamp/reservecamping/campsite/repository/CampsitePricingRuleRepository.java
// Dernière modification : 2026-05-05
//
// Résumé :
// - Repository des règles tarifaires
// - Recherche par site ou regroupement
// - Méthodes de chevauchement pour le calculateur
// - Vérification d’utilisation d’une valeur tarifaire avant suppression
// ============================================================

package com.gocamp.reservecamping.campsite.repository;

import com.gocamp.reservecamping.campsite.model.CampsitePricingRule;
import com.gocamp.reservecamping.campsite.model.PricingTargetType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CampsitePricingRuleRepository extends JpaRepository<CampsitePricingRule, Long> {

    List<CampsitePricingRule> findByCampgroundIdOrderByStartDateAscEndDateAsc(Long campgroundId);

    List<CampsitePricingRule> findByCampsiteIdOrderByStartDateAscEndDateAsc(Long campsiteId);

    List<CampsitePricingRule> findByPricingOptionIdOrderByStartDateAscEndDateAsc(Long pricingOptionId);

    boolean existsByPricingOptionId(Long pricingOptionId);

    boolean existsByCampgroundIdAndTargetTypeAndCampsiteIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            Long campgroundId,
            PricingTargetType targetType,
            Long campsiteId,
            LocalDate endDate,
            LocalDate startDate
    );

    boolean existsByCampgroundIdAndTargetTypeAndPricingOptionIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            Long campgroundId,
            PricingTargetType targetType,
            Long pricingOptionId,
            LocalDate endDate,
            LocalDate startDate
    );

    List<CampsitePricingRule> findByCampgroundIdAndTargetTypeAndCampsiteIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByStartDateAscEndDateAsc(
            Long campgroundId,
            PricingTargetType targetType,
            Long campsiteId,
            LocalDate endDate,
            LocalDate startDate
    );

    List<CampsitePricingRule> findByCampgroundIdAndTargetTypeAndPricingOptionIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByStartDateAscEndDateAsc(
            Long campgroundId,
            PricingTargetType targetType,
            Long pricingOptionId,
            LocalDate endDate,
            LocalDate startDate
    );
}