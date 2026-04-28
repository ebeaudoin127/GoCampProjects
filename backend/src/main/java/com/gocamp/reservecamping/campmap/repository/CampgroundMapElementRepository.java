
// ============================================================
// Fichier : CampgroundMapElementRepository.java
// Chemin : backend/src/main/java/com/gocamp/reservecamping/campmap/repository
// Dernière modification : 2026-04-18
//
// Résumé :
// - Repository des éléments de carte d’un camping
// - Permet de récupérer les éléments non-sites actifs
// ============================================================

package com.gocamp.reservecamping.campmap.repository;

import com.gocamp.reservecamping.campmap.model.CampgroundMapElement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CampgroundMapElementRepository extends JpaRepository<CampgroundMapElement, Long> {

    List<CampgroundMapElement> findByCampgroundIdAndIsActiveTrue(Long campgroundId);
}
