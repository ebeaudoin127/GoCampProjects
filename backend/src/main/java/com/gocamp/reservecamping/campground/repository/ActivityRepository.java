// ============================================================
// Fichier : ActivityRepository.java
// Dernière modification : 2026-04-17
// Auteur : ChatGPT
//
// Résumé :
// - Repository des activités de camping
// ============================================================

package com.gocamp.reservecamping.campground.repository;

import com.gocamp.reservecamping.campground.model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ActivityRepository extends JpaRepository<Activity, Long> {

    List<Activity> findAllByOrderByDisplayOrderAscNameFrAsc();

    List<Activity> findByIsActiveTrueOrderByDisplayOrderAscNameFrAsc();

    Optional<Activity> findByCode(String code);

    boolean existsByCode(String code);
}