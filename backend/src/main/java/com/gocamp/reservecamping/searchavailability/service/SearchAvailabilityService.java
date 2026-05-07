// ============================================================
// Fichier : SearchAvailabilityService.java
// Chemin  : backend/src/main/java/com/gocamp/reservecamping/searchavailability/service
// Dernière modification : 2026-05-07
// Auteur : ChatGPT pour Eric Beaudoin
//
// Résumé :
// - Service principal du moteur de recherche disponibilité
// - Recherche géographique des campgrounds
// - Utilise le repository custom SearchAvailabilityRepositoryCustom
//
// Historique des modifications :
// 2026-05-07
// - Création initiale
// - Ajout recherche géographique
// - Correction injection Spring : utilisation de SearchAvailabilityRepositoryCustom
//   au lieu de SearchAvailabilityRepository
// ============================================================

package com.gocamp.reservecamping.searchavailability.service;

import com.gocamp.reservecamping.searchavailability.repository.NearbyCampgroundProjection;
import com.gocamp.reservecamping.searchavailability.repository.SearchAvailabilityRepositoryCustom;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class SearchAvailabilityService {

    private final SearchAvailabilityRepositoryCustom repository;

    public SearchAvailabilityService(
            SearchAvailabilityRepositoryCustom repository
    ) {
        this.repository = repository;
    }

    public List<NearbyCampgroundProjection> findNearbyCampgrounds(
            BigDecimal latitude,
            BigDecimal longitude,
            BigDecimal radiusKm
    ) {

        return repository.findNearbyCampgrounds(
                latitude,
                longitude,
                radiusKm
        );
    }
}