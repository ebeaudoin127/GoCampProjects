// ============================================================
// Fichier : SearchAvailabilityRowMapper.java
// Chemin  : backend/src/main/java/com/gocamp/reservecamping/searchavailability/mapper
// Dernière modification : 2026-05-14
// Auteur : ChatGPT pour Eric Beaudoin
//
// Résumé :
// - Convertit les résultats SQL natifs Object[] en projections
// - Évite de mapper les colonnes directement dans le repository
// - Ajout services directs du site
// - Ajout ampérages disponibles sur le site
// - Ajout services/activités du camping
// - Ajout dimensions du terrain pour l’écran résultats
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
//
// 2026-05-10
// - Ajout hasWater / hasElectricity / hasSewer
// - Ajout has15_20Amp / has30Amp / has50Amp
// - Ajout campgroundServiceCodes
// - Ajout activityCodes
//
// 2026-05-14
// - Ajout widthFeet
// - Ajout lengthFeet
// - Décalage des index de mapping selon les nouvelles colonnes SQL
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
            public BigDecimal getWidthFeet() {
                return (BigDecimal) row[7];
            }

            @Override
            public BigDecimal getLengthFeet() {
                return (BigDecimal) row[8];
            }

            @Override
            public BigDecimal getMaxEquipmentLengthFeet() {
                return (BigDecimal) row[9];
            }

            @Override
            public String getPhotoUrl1() {
                return (String) row[10];
            }

            @Override
            public String getPhotoUrl2() {
                return (String) row[11];
            }

            @Override
            public String getPhotoUrl3() {
                return (String) row[12];
            }

            @Override
            public Boolean getPullThrough() {
                return toBoolean(row[13]);
            }

            @Override
            public String getServiceTypeCode() {
                return (String) row[14];
            }

            @Override
            public String getServiceTypeNameFr() {
                return (String) row[15];
            }

            @Override
            public String getSurfaceValues() {
                return (String) row[16];
            }

            @Override
            public Boolean getHasWater() {
                return toBoolean(row[17]);
            }

            @Override
            public Boolean getHasElectricity() {
                return toBoolean(row[18]);
            }

            @Override
            public Boolean getHasSewer() {
                return toBoolean(row[19]);
            }

            @Override
            public Boolean getHas15_20Amp() {
                return toBoolean(row[20]);
            }

            @Override
            public Boolean getHas30Amp() {
                return toBoolean(row[21]);
            }

            @Override
            public Boolean getHas50Amp() {
                return toBoolean(row[22]);
            }

            @Override
            public String getCampgroundServiceCodes() {
                return (String) row[23];
            }

            @Override
            public String getActivityCodes() {
                return (String) row[24];
            }
        };
    }

    private Boolean toBoolean(Object value) {
        if (value == null) {
            return false;
        }

        if (value instanceof Boolean booleanValue) {
            return booleanValue;
        }

        if (value instanceof Number numberValue) {
            return numberValue.intValue() == 1;
        }

        return Boolean.parseBoolean(value.toString());
    }
}