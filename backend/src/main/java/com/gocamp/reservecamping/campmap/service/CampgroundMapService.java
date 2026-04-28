// ============================================================
// Fichier : CampgroundMapService.java
// Chemin : backend/src/main/java/com/gocamp/reservecamping/campmap/service
// Dernière modification : 2026-04-18
//
// Résumé :
// - Service de la carte du camping
// - Retourne l'image de fond, les sites et les éléments
// - Ajout du compteur photo pour chaque site
// ============================================================

package com.gocamp.reservecamping.campmap.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gocamp.reservecamping.campground.repository.CampgroundRepository;
import com.gocamp.reservecamping.campsite.repository.CampsitePhotoRepository;
import com.gocamp.reservecamping.campsite.repository.CampsiteRepository;
import com.gocamp.reservecamping.campmap.dto.CampgroundMapElementResponse;
import com.gocamp.reservecamping.campmap.dto.CampgroundMapPointResponse;
import com.gocamp.reservecamping.campmap.dto.CampgroundMapResponse;
import com.gocamp.reservecamping.campmap.dto.CampgroundMapSiteResponse;
import com.gocamp.reservecamping.campmap.repository.CampgroundMapElementRepository;
import com.gocamp.reservecamping.campmap.repository.CampgroundMapRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CampgroundMapService {

    private final CampgroundRepository campgroundRepository;
    private final CampsiteRepository campsiteRepository;
    private final CampsitePhotoRepository campsitePhotoRepository;
    private final CampgroundMapRepository mapRepository;
    private final CampgroundMapElementRepository mapElementRepository;
    private final ObjectMapper objectMapper;

    public CampgroundMapService(
            CampgroundRepository campgroundRepository,
            CampsiteRepository campsiteRepository,
            CampsitePhotoRepository campsitePhotoRepository,
            CampgroundMapRepository mapRepository,
            CampgroundMapElementRepository mapElementRepository,
            ObjectMapper objectMapper
    ) {
        this.campgroundRepository = campgroundRepository;
        this.campsiteRepository = campsiteRepository;
        this.campsitePhotoRepository = campsitePhotoRepository;
        this.mapRepository = mapRepository;
        this.mapElementRepository = mapElementRepository;
        this.objectMapper = objectMapper;
    }

    public CampgroundMapResponse getMap(Long campgroundId) {

        var campground = campgroundRepository.findById(campgroundId)
                .orElseThrow(() -> new RuntimeException("Camping introuvable"));

        var map = mapRepository.findByCampgroundId(campgroundId).orElse(null);

        var sites = campsiteRepository.findByCampgroundIdOrderBySiteCodeAsc(campgroundId)
                .stream()
                .map(c -> new CampgroundMapSiteResponse(
                        c.getId(),
                        c.getSiteCode(),
                        c.isActive(),
                        c.getLabelX(),
                        c.getLabelY(),
                        (int) campsitePhotoRepository.countByCampsiteId(c.getId()),
                        parsePolygon(c.getMapPolygonJson())
                ))
                .toList();

        var elements = mapElementRepository.findByCampgroundIdAndIsActiveTrue(campgroundId)
                .stream()
                .map(e -> new CampgroundMapElementResponse(
                        e.getId(),
                        e.getName(),
                        e.getType() != null ? e.getType().getNameFr() : null,
                        e.getIsActive(),
                        e.getLabelX(),
                        e.getLabelY(),
                        parsePolygon(e.getPolygonJson())
                ))
                .toList();

        return new CampgroundMapResponse(
                campgroundId,
                campground.getName(),
                map != null ? map.getBackgroundImagePath() : null,
                map != null ? map.getImageWidth() : null,
                map != null ? map.getImageHeight() : null,
                sites,
                elements
        );
    }

    private List<CampgroundMapPointResponse> parsePolygon(String json) {
        try {
            if (json == null || json.isBlank()) {
                return Collections.emptyList();
            }

            return objectMapper.readValue(
                    json,
                    new TypeReference<List<CampgroundMapPointResponse>>() {}
            );
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}