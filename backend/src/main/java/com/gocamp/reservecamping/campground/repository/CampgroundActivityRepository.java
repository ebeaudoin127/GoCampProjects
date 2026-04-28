// ============================================================
// Fichier : CampgroundActivityRepository.java
// Dernière modification : 2026-04-17
// Auteur : ChatGPT
//
// Résumé :
// - Repository des associations campground <-> activity
// - CORRECTION : suppression explicite par @Query
// ============================================================

package com.gocamp.reservecamping.campground.repository;

import com.gocamp.reservecamping.campground.model.CampgroundActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CampgroundActivityRepository extends JpaRepository<CampgroundActivity, Long> {

    List<CampgroundActivity> findByCampgroundId(Long campgroundId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("delete from CampgroundActivity ca where ca.campground.id = :campgroundId")
    void deleteAllByCampgroundId(@Param("campgroundId") Long campgroundId);
}