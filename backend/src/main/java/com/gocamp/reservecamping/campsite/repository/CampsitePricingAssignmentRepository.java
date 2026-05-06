// ============================================================
// Fichier : backend/src/main/java/com/gocamp/reservecamping/campsite/repository/CampsitePricingAssignmentRepository.java
// Dernière modification : 2026-05-05
//
// Résumé :
// - Repository de l’affectation d’une valeur de tarification à un site
// - Permet de vérifier si une valeur est utilisée avant suppression
// ============================================================

package com.gocamp.reservecamping.campsite.repository;

import com.gocamp.reservecamping.campsite.model.CampsitePricingAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CampsitePricingAssignmentRepository extends JpaRepository<CampsitePricingAssignment, Long> {

    Optional<CampsitePricingAssignment> findByCampsiteId(Long campsiteId);

    void deleteByCampsiteId(Long campsiteId);

    boolean existsByPricingOptionId(Long pricingOptionId);
}
