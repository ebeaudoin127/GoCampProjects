// ============================================================
// Fichier : SearchAvailabilityService.java
// Chemin  : backend/src/main/java/com/gocamp/reservecamping/searchavailability/service
// Dernière modification : 2026-05-09
// Auteur : ChatGPT pour Eric Beaudoin
//
// Résumé :
// - Service principal du moteur de recherche disponibilité
// - Recherche géographique des campgrounds
// - Génère un résumé intelligent des disponibilités
// - Garde l’aperçu à 5 terrains par camping
// - Ajoute la liste complète des terrains disponibles
// - Utilise l’équipement actif du user comme contexte par défaut
// - Applique les filtres avancés : services, accès direct, surfaces
//
// Historique des modifications :
// 2026-05-07
// - Création initiale
//
// 2026-05-08
// - Ajout buildSearchSummary()
// - Ajout groupement par campground
//
// 2026-05-09
// - Ajout contexte équipement
// - Ajout photos
// - Ajout filtres avancés
// - Ajout allCampsites pour affichage complet paginé côté frontend
// ============================================================

package com.gocamp.reservecamping.searchavailability.service;

import com.gocamp.reservecamping.equipement.EquipementVR;
import com.gocamp.reservecamping.equipement.EquipementVRRepository;
import com.gocamp.reservecamping.searchavailability.dto.AvailableCampsiteSearchRequest;
import com.gocamp.reservecamping.searchavailability.dto.SearchAvailabilitySummaryResponse;
import com.gocamp.reservecamping.searchavailability.dto.SearchCampgroundSummaryDto;
import com.gocamp.reservecamping.searchavailability.dto.SearchCampsitePreviewDto;
import com.gocamp.reservecamping.searchavailability.dto.SearchCampsiteResultDto;
import com.gocamp.reservecamping.searchavailability.repository.AvailableCampsiteSearchRowProjection;
import com.gocamp.reservecamping.searchavailability.repository.NearbyCampgroundProjection;
import com.gocamp.reservecamping.searchavailability.repository.SearchAvailabilityRepositoryCustom;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.Normalizer;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class SearchAvailabilityService {

    private static final int PREVIEW_MAX_ROWS = 40;
    private static final int PREVIEW_CAMPSITES_PER_CAMPGROUND = 5;

    private final SearchAvailabilityRepositoryCustom repository;
    private final EquipementVRRepository equipementVRRepository;

    public SearchAvailabilityService(
            SearchAvailabilityRepositoryCustom repository,
            EquipementVRRepository equipementVRRepository
    ) {
        this.repository = repository;
        this.equipementVRRepository = equipementVRRepository;
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

    public SearchAvailabilitySummaryResponse buildSearchSummary(
            AvailableCampsiteSearchRequest request
    ) {
        validateSearchRequest(request);

        BigDecimal equipmentLengthFeet =
                resolveEquipmentLengthFeet(request);

        applyEquipmentDefaultSearchPreferences(request);

        List<AvailableCampsiteSearchRowProjection> rows =
                repository.findAvailableCampsiteRows(
                        request.getArrivalDate(),
                        request.getDepartureDate(),
                        request.getLatitude(),
                        request.getLongitude(),
                        request.getRadiusKm(),
                        request.getCampgroundId(),
                        equipmentLengthFeet
                );

        Map<Long, SearchCampgroundSummaryDto> grouped =
                new LinkedHashMap<>();

        List<SearchCampsiteResultDto> allCampsites =
                new ArrayList<>();

        int totalCampsites = 0;
        int previewRowsUsed = 0;

        for (AvailableCampsiteSearchRowProjection row : rows) {
            if (!matchesAdvancedFilters(row, request)) {
                continue;
            }

            totalCampsites++;

            allCampsites.add(
                    createCampsiteResult(row)
            );

            SearchCampgroundSummaryDto campgroundDto =
                    grouped.computeIfAbsent(
                            row.getCampgroundId(),
                            id -> createCampgroundSummary(row)
                    );

            campgroundDto.setAvailableCampsiteCount(
                    campgroundDto.getAvailableCampsiteCount() + 1
            );

            boolean canAddPreviewForCampground =
                    campgroundDto.getPreviewCampsites().size()
                            < PREVIEW_CAMPSITES_PER_CAMPGROUND;

            boolean canAddPreviewGlobally =
                    previewRowsUsed < PREVIEW_MAX_ROWS;

            if (canAddPreviewForCampground && canAddPreviewGlobally) {
                campgroundDto.getPreviewCampsites().add(
                        createCampsitePreview(row)
                );

                previewRowsUsed++;
            }
        }

        SearchAvailabilitySummaryResponse response =
                new SearchAvailabilitySummaryResponse();

        response.setTotalCampgrounds(grouped.size());
        response.setTotalCampsites(totalCampsites);
        response.setPreviewMaxRows(PREVIEW_MAX_ROWS);
        response.setPreviewCampsitesPerCampground(
                PREVIEW_CAMPSITES_PER_CAMPGROUND
        );
        response.setCampgrounds(
                grouped.values().stream().toList()
        );
        response.setAllCampsites(allCampsites);

        return response;
    }

    private void applyEquipmentDefaultSearchPreferences(
            AvailableCampsiteSearchRequest request
    ) {
        boolean useEquipmentContext =
                !Boolean.FALSE.equals(request.getUseEquipmentContext());

        if (!useEquipmentContext || request.getUserId() == null) {
            return;
        }

        EquipementVR activeEquipment =
                findActiveEquipment(
                        equipementVRRepository.findByUserId(request.getUserId())
                );

        if (activeEquipment == null) {
            return;
        }

        request.setRequiresWater(
                Boolean.TRUE.equals(activeEquipment.getDefaultRequiresWater())
        );

        request.setRequiresSewer(
                Boolean.TRUE.equals(activeEquipment.getDefaultRequiresSewer())
        );

        request.setRequiresElectricity(
                Boolean.TRUE.equals(activeEquipment.getDefaultRequiresElectricity())
                        || Boolean.TRUE.equals(activeEquipment.getDefaultRequires15_20Amp())
                        || Boolean.TRUE.equals(activeEquipment.getDefaultRequires30Amp())
                        || Boolean.TRUE.equals(activeEquipment.getDefaultRequires50Amp())
        );

        request.setRequires15_20Amp(
                Boolean.TRUE.equals(activeEquipment.getDefaultRequires15_20Amp())
        );

        request.setRequires30Amp(
                Boolean.TRUE.equals(activeEquipment.getDefaultRequires30Amp())
        );

        request.setRequires50Amp(
                Boolean.TRUE.equals(activeEquipment.getDefaultRequires50Amp())
        );
    }

    private boolean matchesAdvancedFilters(
            AvailableCampsiteSearchRowProjection row,
            AvailableCampsiteSearchRequest request
    ) {
        if (
                Boolean.TRUE.equals(request.getPullThroughOnly())
                        && !Boolean.TRUE.equals(row.getPullThrough())
        ) {
            return false;
        }

        String serviceText = normalize(
                safe(row.getServiceTypeCode()) + " " +
                        safe(row.getServiceTypeNameFr())
        );

        if (
                Boolean.TRUE.equals(request.getRequiresWater())
                        && !containsAny(serviceText, "eau", "water")
        ) {
            return false;
        }

        if (
                Boolean.TRUE.equals(request.getRequiresSewer())
                        && !containsAny(serviceText, "egout", "sewer")
        ) {
            return false;
        }

        if (Boolean.TRUE.equals(request.getRequiresElectricity())) {
            boolean hasMatchingAmp =
                    Boolean.TRUE.equals(request.getRequires15_20Amp())
                            || Boolean.TRUE.equals(request.getRequires30Amp())
                            || Boolean.TRUE.equals(request.getRequires50Amp());

            if (!hasMatchingAmp) {
                return false;
            }
        }

        if (
                request.getSurfaceTypes() != null
                        && !request.getSurfaceTypes().isEmpty()
        ) {
            String surfaceText = normalize(row.getSurfaceValues());

            boolean hasMatchingSurface = request.getSurfaceTypes()
                    .stream()
                    .map(this::normalize)
                    .anyMatch(surfaceText::contains);

            if (!hasMatchingSurface) {
                return false;
            }
        }

        return true;
    }

    private BigDecimal resolveEquipmentLengthFeet(
            AvailableCampsiteSearchRequest request
    ) {
        boolean useEquipmentContext =
                !Boolean.FALSE.equals(request.getUseEquipmentContext());

        if (!useEquipmentContext) {
            return null;
        }

        if (request.getEquipmentLengthFeet() != null) {
            return request.getEquipmentLengthFeet();
        }

        if (request.getUserId() == null) {
            return null;
        }

        List<EquipementVR> equipements =
                equipementVRRepository.findByUserId(request.getUserId());

        EquipementVR activeEquipment =
                findActiveEquipment(equipements);

        if (
                activeEquipment == null
                        || activeEquipment.getLongueur() == null
        ) {
            return null;
        }

        return BigDecimal.valueOf(activeEquipment.getLongueur());
    }

    private EquipementVR findActiveEquipment(
            List<EquipementVR> equipements
    ) {
        if (equipements == null || equipements.isEmpty()) {
            return null;
        }

        for (EquipementVR equipement : equipements) {
            if (Boolean.TRUE.equals(equipement.getActif())) {
                return equipement;
            }
        }

        return equipements.get(0);
    }

    private void validateSearchRequest(
            AvailableCampsiteSearchRequest request
    ) {
        if (request == null) {
            throw new RuntimeException(
                    "La requête de recherche est obligatoire"
            );
        }

        LocalDate arrivalDate = request.getArrivalDate();
        LocalDate departureDate = request.getDepartureDate();

        if (arrivalDate == null || departureDate == null) {
            throw new RuntimeException(
                    "Les dates d’arrivée et de départ sont obligatoires"
            );
        }

        if (
                departureDate.isBefore(arrivalDate)
                        || departureDate.isEqual(arrivalDate)
        ) {
            throw new RuntimeException(
                    "La date de départ doit être après la date d’arrivée"
            );
        }

        if (
                request.getCampgroundId() == null
                        && (
                        request.getLatitude() == null
                                || request.getLongitude() == null
                                || request.getRadiusKm() == null
                )
        ) {
            throw new RuntimeException(
                    "Vous devez fournir un camping précis ou une position GPS avec un rayon"
            );
        }
    }

    private SearchCampgroundSummaryDto createCampgroundSummary(
            AvailableCampsiteSearchRowProjection row
    ) {
        SearchCampgroundSummaryDto dto =
                new SearchCampgroundSummaryDto();

        dto.setCampgroundId(row.getCampgroundId());
        dto.setCampgroundName(row.getCampgroundName());
        dto.setLatitude(row.getGpsLatitude());
        dto.setLongitude(row.getGpsLongitude());
        dto.setDistanceKm(row.getDistanceKm());
        dto.setAvailableCampsiteCount(0);

        return dto;
    }

    private SearchCampsitePreviewDto createCampsitePreview(
            AvailableCampsiteSearchRowProjection row
    ) {
        SearchCampsitePreviewDto dto =
                new SearchCampsitePreviewDto();

        dto.setCampsiteId(row.getCampsiteId());
        dto.setSiteCode(row.getSiteCode());
        dto.setMaxEquipmentLengthFeet(
                row.getMaxEquipmentLengthFeet()
        );

        dto.setPhotoUrls(buildPhotoUrls(row));

        return dto;
    }

    private SearchCampsiteResultDto createCampsiteResult(
            AvailableCampsiteSearchRowProjection row
    ) {
        SearchCampsiteResultDto dto =
                new SearchCampsiteResultDto();

        dto.setCampgroundId(row.getCampgroundId());
        dto.setCampgroundName(row.getCampgroundName());
        dto.setDistanceKm(row.getDistanceKm());
        dto.setCampsiteId(row.getCampsiteId());
        dto.setSiteCode(row.getSiteCode());
        dto.setMaxEquipmentLengthFeet(
                row.getMaxEquipmentLengthFeet()
        );
        dto.setPhotoUrls(buildPhotoUrls(row));

        return dto;
    }

    private List<String> buildPhotoUrls(
            AvailableCampsiteSearchRowProjection row
    ) {
        List<String> urls = new ArrayList<>();

        addIfPresent(urls, row.getPhotoUrl1());
        addIfPresent(urls, row.getPhotoUrl2());
        addIfPresent(urls, row.getPhotoUrl3());

        return urls;
    }

    private void addIfPresent(
            List<String> urls,
            String value
    ) {
        if (value != null && !value.isBlank()) {
            urls.add(value);
        }
    }

    private boolean containsAny(
            String source,
            String... values
    ) {
        for (String value : values) {
            if (source.contains(normalize(value))) {
                return true;
            }
        }

        return false;
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }

    private String normalize(String value) {
        if (value == null) {
            return "";
        }

        String normalized = Normalizer.normalize(
                value,
                Normalizer.Form.NFD
        );

        return normalized
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT)
                .trim();
    }
}