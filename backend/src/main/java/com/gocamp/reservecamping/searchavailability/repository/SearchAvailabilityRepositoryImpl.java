// ============================================================
// Fichier : SearchAvailabilityRepositoryImpl.java
// Chemin  : backend/src/main/java/com/gocamp/reservecamping/searchavailability/repository
// Dernière modification : 2026-05-07
// Auteur : ChatGPT pour Eric Beaudoin
//
// Résumé :
// - Implémentation SQL custom du moteur de recherche disponibilité
// - Recherche géographique avec formule Haversine
//
// Historique des modifications :
// 2026-05-07
// - Création initiale
// - Ajout recherche géographique Haversine
// ============================================================

package com.gocamp.reservecamping.searchavailability.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Repository
public class SearchAvailabilityRepositoryImpl
        implements SearchAvailabilityRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<NearbyCampgroundProjection> findNearbyCampgrounds(
            BigDecimal latitude,
            BigDecimal longitude,
            BigDecimal radiusKm
    ) {

        String sql = """
                SELECT
                    c.id,
                    c.name,
                    c.gps_latitude,
                    c.gps_longitude,

                    (
                        6371 * acos(
                            cos(radians(:latitude))
                            * cos(radians(c.gps_latitude))
                            * cos(radians(c.gps_longitude) - radians(:longitude))
                            + sin(radians(:latitude))
                            * sin(radians(c.gps_latitude))
                        )
                    ) AS distance_km

                FROM campground c

                WHERE c.gps_latitude IS NOT NULL
                  AND c.gps_longitude IS NOT NULL

                HAVING distance_km <= :radiusKm

                ORDER BY distance_km ASC
                """;

        Query query = entityManager.createNativeQuery(sql);

        query.setParameter("latitude", latitude);
        query.setParameter("longitude", longitude);
        query.setParameter("radiusKm", radiusKm);

        List<Object[]> rows = query.getResultList();

        List<NearbyCampgroundProjection> results = new ArrayList<>();

        for (Object[] row : rows) {

            results.add(
                    new NearbyCampgroundProjection() {

                        @Override
                        public Long getCampgroundId() {
                            return ((Number) row[0]).longValue();
                        }

                        @Override
                        public String getCampgroundName() {
                            return (String) row[1];
                        }

                        @Override
                        public BigDecimal getGpsLatitude() {
                            return (BigDecimal) row[2];
                        }

                        @Override
                        public BigDecimal getGpsLongitude() {
                            return (BigDecimal) row[3];
                        }

                        @Override
                        public Double getDistanceKm() {
                            return ((Number) row[4]).doubleValue();
                        }
                    }
            );
        }

        return results;
    }
}