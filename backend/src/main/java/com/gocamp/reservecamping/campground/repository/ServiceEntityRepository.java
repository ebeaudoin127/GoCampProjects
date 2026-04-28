// ============================================================
// Fichier : ServiceEntityRepository.java
// Dernière modification : 2026-04-17
// Auteur : ChatGPT
//
// Résumé :
// - Repository des services de camping
// ============================================================

package com.gocamp.reservecamping.campground.repository;

import com.gocamp.reservecamping.campground.model.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServiceEntityRepository extends JpaRepository<ServiceEntity, Long> {

    List<ServiceEntity> findAllByOrderByDisplayOrderAscNameFrAsc();

    List<ServiceEntity> findByIsActiveTrueOrderByDisplayOrderAscNameFrAsc();

    Optional<ServiceEntity> findByCode(String code);

    boolean existsByCode(String code);
}