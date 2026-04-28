// ============================================================
// Fichier : CampsiteEquipmentAllowedRepository.java
// Chemin : backend/src/main/java/com/gocamp/reservecamping/campsite/repository
// Dernière modification : 2026-04-18
//
// Résumé :
// - Repository des équipements autorisés d’un site
// - CORRECTION : suppression explicite avec @Query
// ============================================================

package com.gocamp.reservecamping.campsite.repository;

import com.gocamp.reservecamping.campsite.model.CampsiteEquipmentAllowed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CampsiteEquipmentAllowedRepository extends JpaRepository<CampsiteEquipmentAllowed, Long> {

    List<CampsiteEquipmentAllowed> findByCampsiteId(Long campsiteId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("delete from CampsiteEquipmentAllowed cea where cea.campsite.id = :campsiteId")
    void deleteAllByCampsiteId(@Param("campsiteId") Long campsiteId);
}