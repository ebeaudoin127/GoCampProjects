// ============================================================
// Fichier : SearchAvailabilitySqlSelector.java
// Chemin  : backend/src/main/java/com/gocamp/reservecamping/searchavailability/selector
// Dernière modification : 2026-05-14
// Auteur : ChatGPT pour Eric Beaudoin
//
// Résumé :
// - Centralise les requêtes SQL natives du moteur de recherche
// - Conserve les méthodes attendues par SearchAvailabilityRepositoryImpl
// - Ajout des services directs du site
// - Ajout des ampérages disponibles sur le site
// - Ajout services/activités du camping
// - Ajout dimensions du terrain pour l’écran résultats
//
// Historique des modifications :
// 2026-05-08
// - Création initiale du selector SQL
//
// 2026-05-09
// - Ajout filtre equipmentLengthFeet
// - Ajout photos
// - Ajout service type, pull-through et surfaces
//
// 2026-05-10
// - Ajout hasWater / hasElectricity / hasSewer
// - Ajout has15_20Amp / has30Amp / has50Amp
// - Ajout campgroundServiceCodes
// - Ajout activityCodes
//
// 2026-05-14
// - Ajout width_feet
// - Ajout length_feet
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
                    cs.width_feet,
                    cs.length_feet,
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
                    ) AS surface_values,
                    cs.has_water,
                    cs.has_electricity,
                    cs.has_sewer,
                    cs.has_15_20_amp,
                    cs.has_30_amp,
                    cs.has_50_amp,
                    (
                        SELECT GROUP_CONCAT(DISTINCT srv.code SEPARATOR ',')
                        FROM campground_service csrv
                        INNER JOIN service srv
                            ON srv.id = csrv.service_id
                        WHERE csrv.campground_id = cg.id
                    ) AS campground_service_codes,
                    (
                        SELECT GROUP_CONCAT(DISTINCT act.code SEPARATOR ',')
                        FROM campground_activity ca
                        INNER JOIN activity act
                            ON act.id = ca.activity_id
                        WHERE ca.campground_id = cg.id
                    ) AS activity_codes
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
                HAVING :campgroundId IS NOT NULL
                    OR :radiusKm IS NULL
                    OR distance_km <= :radiusKm
                ORDER BY distance_km ASC, cg.name ASC, cs.site_code ASC
                """;
    }
}