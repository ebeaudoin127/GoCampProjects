// ============================================================
// Fichier : CampgroundMapRepository.java
// Chemin : campmap/repository
// ============================================================

package com.gocamp.reservecamping.campmap.repository;

import com.gocamp.reservecamping.campmap.model.CampgroundMap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CampgroundMapRepository extends JpaRepository<CampgroundMap, Long> {

    Optional<CampgroundMap> findByCampgroundId(Long campgroundId);
}