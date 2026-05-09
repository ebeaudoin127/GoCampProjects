// ============================================================
// Fichier : SearchAvailabilityRowMapper.java
// Chemin  : backend/src/main/java/com/gocamp/reservecamping/searchavailability/mapper
// Dernière modification : 2026-05-09
// Auteur : ChatGPT pour Eric Beaudoin
//
// Résumé :
// - Convertit les résultats SQL natifs Object[] en projections
// - Évite de mapper les colonnes directement dans le repository
//
// Historique des modifications :
// 2026-05-08
// - Création initiale du mapper
//
// 2026-05-09
// - Ajout photos
// - Ajout pullThrough
// - Ajout service type
// - Ajout surfaceValues
// ============================================================

package com.gocamp.reservecamping.searchavailability.mapper;

import com.gocamp.reservecamping.searchavailability.repository.AvailableCampsiteSearchRowProjection;
import com.gocamp.reservecamping.searchavailability.repository.NearbyCampgroundProjection;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class SearchAvailabilityRowMapper {

    public NearbyCampgroundProjection toNearbyCampgroundProjection(
            Object[] row
    ) {
        return new NearbyCampgroundProjection() {

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
        };
    }

    public AvailableCampsiteSearchRowProjection toAvailableCampsiteSearchRowProjection(
            Object[] row
    ) {
        return new AvailableCampsiteSearchRowProjection() {

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

            @Override
            public Long getCampsiteId() {
                return ((Number) row[5]).longValue();
            }

            @Override
            public String getSiteCode() {
                return (String) row[6];
            }

            @Override
            public BigDecimal getMaxEquipmentLengthFeet() {
                return (BigDecimal) row[7];
            }

            @Override
            public String getPhotoUrl1() {
                return (String) row[8];
            }

            @Override
            public String getPhotoUrl2() {
                return (String) row[9];
            }

            @Override
            public String getPhotoUrl3() {
                return (String) row[10];
            }

            @Override
            public Boolean getPullThrough() {
                if (row[11] == null) {
                    return false;
                }

                if (row[11] instanceof Boolean value) {
                    return value;
                }

                if (row[11] instanceof Number value) {
                    return value.intValue() == 1;
                }

                return Boolean.parseBoolean(row[11].toString());
            }

            @Override
            public String getServiceTypeCode() {
                return (String) row[12];
            }

            @Override
            public String getServiceTypeNameFr() {
                return (String) row[13];
            }

            @Override
            public String getSurfaceValues() {
                return (String) row[14];
            }
        };
    }
}
