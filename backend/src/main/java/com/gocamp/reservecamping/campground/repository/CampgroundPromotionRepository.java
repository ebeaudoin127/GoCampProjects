// ============================================================
// Fichier : CampgroundPromotionRepository.java
// Dernière modification : 2026-04-17
// Auteur : ChatGPT
//
// Résumé :
// - Repository des promotions de camping
// ============================================================

package com.gocamp.reservecamping.campground.repository;

import com.gocamp.reservecamping.campground.model.CampgroundPromotion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CampgroundPromotionRepository extends JpaRepository<CampgroundPromotion, Long> {

    List<CampgroundPromotion> findByCampgroundIdOrderByStartDateDesc(Long campgroundId);

    List<CampgroundPromotion> findByCampgroundIdAndIsActiveTrueOrderByStartDateDesc(Long campgroundId);
}