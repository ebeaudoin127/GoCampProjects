// ============================================================
// Fichier : CampsiteSurfaceTypeRepository.java
// Chemin : backend/src/main/java/com/gocamp/reservecamping/campsite/repository
// Dernière modification : 2026-04-18
//
// Résumé :
// - Repository des surfaces d’un site
// - CORRECTION : suppression explicite avec @Query
// ============================================================

package com.gocamp.reservecamping.campsite.repository;

import com.gocamp.reservecamping.campsite.model.CampsiteSurfaceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CampsiteSurfaceTypeRepository extends JpaRepository<CampsiteSurfaceType, Long> {

    List<CampsiteSurfaceType> findByCampsiteId(Long campsiteId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("delete from CampsiteSurfaceType cst where cst.campsite.id = :campsiteId")
    void deleteAllByCampsiteId(@Param("campsiteId") Long campsiteId);
}