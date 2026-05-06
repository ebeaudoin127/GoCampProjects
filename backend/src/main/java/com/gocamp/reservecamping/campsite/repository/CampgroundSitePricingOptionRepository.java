// ============================================================
// Fichier : backend/src/main/java/com/gocamp/reservecamping/campsite/repository/CampgroundSitePricingOptionRepository.java
// Dernière modification : 2026-05-05
//
// Résumé :
// - Repository des valeurs de tarification par camping
// - Recherche par camping
// - Recherche par id/camping
// ============================================================

package com.gocamp.reservecamping.campsite.repository;

import com.gocamp.reservecamping.campsite.model.CampgroundSitePricingOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CampgroundSitePricingOptionRepository extends JpaRepository<CampgroundSitePricingOption, Long> {

    List<CampgroundSitePricingOption> findByCampgroundIdAndIsActiveTrueOrderByNameAsc(Long campgroundId);

    Optional<CampgroundSitePricingOption> findByIdAndCampgroundId(Long id, Long campgroundId);
}
