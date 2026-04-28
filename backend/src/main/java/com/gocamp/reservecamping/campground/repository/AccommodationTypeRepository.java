// ============================================================
// Fichier : AccommodationTypeRepository.java
// Dernière modification : 2026-04-17
// Auteur : ChatGPT
//
// Résumé :
// - Repository des types d’hébergement
// ============================================================

package com.gocamp.reservecamping.campground.repository;

import com.gocamp.reservecamping.campground.model.AccommodationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccommodationTypeRepository extends JpaRepository<AccommodationType, Long> {

    List<AccommodationType> findAllByOrderByDisplayOrderAscNameFrAsc();

    List<AccommodationType> findByIsActiveTrueOrderByDisplayOrderAscNameFrAsc();

    List<AccommodationType> findByCategoryOrderByDisplayOrderAscNameFrAsc(String category);

    Optional<AccommodationType> findByCode(String code);

    boolean existsByCode(String code);
}