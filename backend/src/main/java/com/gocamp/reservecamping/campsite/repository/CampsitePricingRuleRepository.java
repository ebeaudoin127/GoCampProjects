// ============================================================
// Fichier : backend/src/main/java/com/gocamp/reservecamping/campsite/repository/CampsitePricingRuleRepository.java
// Dernière modification : 2026-04-20
//
// Résumé :
// - Repository des règles tarifaires
// - Ajout des méthodes de recherche détaillée des chevauchements
// - Conserve la logique existante
// - Ajout d’une méthode utilitaire pour le moteur de prix
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
