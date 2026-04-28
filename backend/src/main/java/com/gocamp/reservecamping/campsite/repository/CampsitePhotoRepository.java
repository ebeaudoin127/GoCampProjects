// ============================================================
// Fichier : CampsitePhotoRepository.java
// Chemin : backend/src/main/java/com/gocamp/reservecamping/campsite/repository
// Dernière modification : 2026-04-18
//
// Résumé :
// - Repository des photos de site
// - Permet listage, comptage et gestion de la photo principale
// ============================================================

package com.gocamp.reservecamping.campsite.repository;

import com.gocamp.reservecamping.campsite.model.CampsitePhoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CampsitePhotoRepository extends JpaRepository<CampsitePhoto, Long> {

    List<CampsitePhoto> findByCampsiteIdOrderByDisplayOrderAscIdAsc(Long campsiteId);

    long countByCampsiteId(Long campsiteId);

    Optional<CampsitePhoto> findByCampsiteIdAndIsPrimaryTrue(Long campsiteId);
}