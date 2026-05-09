// ============================================================
// Fichier : SearchAvailabilitySqlSelector.java
// Chemin  : backend/src/main/java/com/gocamp/reservecamping/searchavailability/selector
// Dernière modification : 2026-05-09
// Auteur : ChatGPT pour Eric Beaudoin
//
// Résumé :
// - Centralise les requêtes SQL natives du moteur de recherche
// - Ajout des données nécessaires aux filtres avancés
//
// Historique des modifications :
// 2026-05-08
// - Création initiale du selector SQL
//
// 2026-05-09
// - Ajout filtre equipmentLengthFeet
// - Ajout photos
// - Ajout service type, pull-through et surfaces
// ============================================================

package com.gocamp.reservecamping.searchavailability.selector;

import org.springframework.stereotype.Component;

@Component
public class SearchAvailabilitySqlSelector {

    public String nearbyCampgroundsSql() {
        return """
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
    }

    public String availableCampsiteRowsSql() {
        return """
                SELECT
                    cg.id,
                    cg.name,
                    cg.gps_latitude,
                    cg.gps_longitude,
                    CASE
                        WHEN :latitude IS NOT NULL
                         AND :longitude IS NOT NULL
                        THEN (
                            6371 * acos(
                                cos(radians(:latitude))
                                * cos(radians(cg.gps_latitude))
                                * cos(radians(cg.gps_longitude) - radians(:longitude))
                                + sin(radians(:latitude))
                                * sin(radians(cg.gps_latitude))
                            )
                        )
                        ELSE 0
                    END AS distance_km,

                    cs.id,
                    cs.site_code,
                    cs.max_equipment_length_feet,

                    (
                        SELECT cp.thumbnail_path
                        FROM campsite_photo cp
                        WHERE cp.campsite_id = cs.id
                          AND cp.is_active = true
                        ORDER BY cp.is_primary DESC, cp.display_order ASC, cp.id ASC
                        LIMIT 1 OFFSET 0
                    ) AS photo_url_1,

                    (
                        SELECT cp.thumbnail_path
                        FROM campsite_photo cp
                        WHERE cp.campsite_id = cs.id
                          AND cp.is_active = true
                        ORDER BY cp.is_primary DESC, cp.display_order ASC, cp.id ASC
                        LIMIT 1 OFFSET 1
                    ) AS photo_url_2,

                    (
                        SELECT cp.thumbnail_path
                        FROM campsite_photo cp
                        WHERE cp.campsite_id = cs.id
                          AND cp.is_active = true
                        ORDER BY cp.is_primary DESC, cp.display_order ASC, cp.id ASC
                        LIMIT 1 OFFSET 2
                    ) AS photo_url_3,

                    cs.is_pull_through,
                    sst.code,
                    sst.name_fr,

                    (
                        SELECT GROUP_CONCAT(
                            CONCAT_WS('|', st.code, st.name_fr)
                            SEPARATOR ';;'
                        )
                        FROM campsite_surface_type cst
                        INNER JOIN site_surface_type st
                            ON st.id = cst.site_surface_type_id
                        WHERE cst.campsite_id = cs.id
                    ) AS surface_values

                FROM campground cg
                INNER JOIN campsite cs
                    ON cs.campground_id = cg.id
                LEFT JOIN site_service_type sst
                    ON sst.id = cs.site_service_type_id

                WHERE cs.is_active = true
                  AND (:campgroundId IS NULL OR cg.id = :campgroundId)
                  AND cg.gps_latitude IS NOT NULL
                  AND cg.gps_longitude IS NOT NULL
                  AND (
                        :equipmentLengthFeet IS NULL
                        OR cs.max_equipment_length_feet IS NULL
                        OR cs.max_equipment_length_feet >= :equipmentLengthFeet
                  )
                  AND NOT EXISTS (
                      SELECT 1
                      FROM reservations r
                      WHERE r.campsite_id = cs.id
                        AND r.status IN ('PENDING', 'CONFIRMED')
                        AND r.arrival_date < :departureDate
                        AND r.departure_date > :arrivalDate
                  )

                HAVING
                    :campgroundId IS NOT NULL
                    OR :radiusKm IS NULL
                    OR distance_km <= :radiusKm

                ORDER BY distance_km ASC, cg.name ASC, cs.site_code ASC
                """;
    }
}