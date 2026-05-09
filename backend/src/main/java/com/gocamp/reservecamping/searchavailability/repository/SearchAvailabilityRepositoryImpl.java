// ============================================================
// Fichier : SearchAvailabilityRepositoryImpl.java
// Chemin  : backend/src/main/java/com/gocamp/reservecamping/searchavailability/repository
// Dernière modification : 2026-05-09
// Auteur : ChatGPT pour Eric Beaudoin
//
// Résumé :
// - Implémentation custom du moteur de recherche disponibilité
// - Exécute les requêtes SQL natives
// - Délègue les requêtes au selector
// - Délègue le mapping au mapper
// - Supporte le filtre longueur équipement
//
// Historique des modifications :
// 2026-05-07
// - Création initiale
// - Ajout recherche géographique Haversine
//
// 2026-05-08
// - Ajout recherche des terrains disponibles
// - Refactor : extraction SQL vers SearchAvailabilitySqlSelector
// - Refactor : extraction mapping vers SearchAvailabilityRowMapper
//
// 2026-05-09
// - Ajout equipmentLengthFeet dans findAvailableCampsiteRows()
// ============================================================

package com.gocamp.reservecamping.searchavailability.repository;

import com.gocamp.reservecamping.searchavailability.mapper.SearchAvailabilityRowMapper;
import com.gocamp.reservecamping.searchavailability.selector.SearchAvailabilitySqlSelector;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public class SearchAvailabilityRepositoryImpl
        implements SearchAvailabilityRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    private final SearchAvailabilitySqlSelector sqlSelector;
    private final SearchAvailabilityRowMapper rowMapper;

    public SearchAvailabilityRepositoryImpl(
            SearchAvailabilitySqlSelector sqlSelector,
            SearchAvailabilityRowMapper rowMapper
    ) {
        this.sqlSelector = sqlSelector;
        this.rowMapper = rowMapper;
    }

    @Override
    public List<NearbyCampgroundProjection> findNearbyCampgrounds(
            BigDecimal latitude,
            BigDecimal longitude,
            BigDecimal radiusKm
    ) {
        Query query = entityManager.createNativeQuery(
                sqlSelector.nearbyCampgroundsSql()
        );

        query.setParameter("latitude", latitude);
        query.setParameter("longitude", longitude);
        query.setParameter("radiusKm", radiusKm);

        List<Object[]> rows = query.getResultList();

        return rows.stream()
                .map(rowMapper::toNearbyCampgroundProjection)
                .toList();
    }

    @Override
    public List<AvailableCampsiteSearchRowProjection> findAvailableCampsiteRows(
            LocalDate arrivalDate,
            LocalDate departureDate,
            BigDecimal latitude,
            BigDecimal longitude,
            BigDecimal radiusKm,
            Long campgroundId,
            BigDecimal equipmentLengthFeet
    ) {
        Query query = entityManager.createNativeQuery(
                sqlSelector.availableCampsiteRowsSql()
        );

        query.setParameter("arrivalDate", arrivalDate);
        query.setParameter("departureDate", departureDate);
        query.setParameter("latitude", latitude);
        query.setParameter("longitude", longitude);
        query.setParameter("radiusKm", radiusKm);
        query.setParameter("campgroundId", campgroundId);
        query.setParameter("equipmentLengthFeet", equipmentLengthFeet);

        List<Object[]> rows = query.getResultList();

        return rows.stream()
                .map(rowMapper::toAvailableCampsiteSearchRowProjection)
                .toList();
    }
}
