// ============================================================
// Fichier : CampgroundRepository.java
// Dernière modification : 2026-04-17
// Auteur : ChatGPT
//
// Résumé :
// - Repository principal des campings
// ============================================================

package com.gocamp.reservecamping.campground.repository;

import com.gocamp.reservecamping.campground.model.Campground;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CampgroundRepository extends JpaRepository<Campground, Long> {

    List<Campground> findAllByOrderByNameAsc();

    List<Campground> findByIsActiveTrueOrderByNameAsc();

    List<Campground> findByCountryIdOrderByNameAsc(Long countryId);

    List<Campground> findByProvinceStateIdOrderByNameAsc(Long provinceStateId);

    Optional<Campground> findByNameAndCity(String name, String city);

    boolean existsByNameAndCity(String name, String city);
}