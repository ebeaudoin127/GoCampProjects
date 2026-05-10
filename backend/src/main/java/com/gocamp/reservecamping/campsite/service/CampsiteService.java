// ============================================================
// Fichier : CampsiteService.java
// Chemin  : backend/src/main/java/com/gocamp/reservecamping/campsite/service
// Dernière modification : 2026-05-09
// Auteur : ChatGPT pour Eric Beaudoin
//
// Résumé :
// - Service métier des sites
// - Gestion création / modification
// - Gestion polygone de carte
// - Gestion tarification par site
// - Ajout services disponibles et ampérages disponibles
//
// Historique des modifications :
// 2026-04-20
// - Ajout pricingOptionId / pricingOptionName
// - Correction JPA detached entity passed to persist
//
// 2026-04-29
// - Ajout deleteMapShape(Long id)
//
// 2026-05-09
// - Ajout sauvegarde hasWater / hasElectricity / hasSewer
// - Ajout sauvegarde has15_20Amp / has30Amp / has50Amp
// - Ajout des nouveaux champs dans CampsiteDetailsResponse
// - Ajout des nouveaux champs dans CampsiteResponse
// ============================================================

package com.gocamp.reservecamping.campsite.service;

import com.gocamp.reservecamping.campground.model.Campground;
import com.gocamp.reservecamping.campground.repository.CampgroundRepository;
import com.gocamp.reservecamping.campsite.dto.CampsiteDetailsResponse;
import com.gocamp.reservecamping.campsite.dto.CampsiteResponse;
import com.gocamp.reservecamping.campsite.dto.CreateCampsiteRequest;
import com.gocamp.reservecamping.campsite.dto.UpdateCampsiteMapShapeRequest;
import com.gocamp.reservecamping.campsite.dto.UpdateCampsiteRequest;
import com.gocamp.reservecamping.campsite.model.CampgroundSitePricingOption;
import com.gocamp.reservecamping.campsite.model.Campsite;
import com.gocamp.reservecamping.campsite.model.CampsiteEquipmentAllowed;
import com.gocamp.reservecamping.campsite.model.CampsitePricingAssignment;
import com.gocamp.reservecamping.campsite.model.CampsiteSurfaceType;
import com.gocamp.reservecamping.campsite.model.EquipmentAllowedType;
import com.gocamp.reservecamping.campsite.model.SiteSurfaceType;
import com.gocamp.reservecamping.campsite.repository.CampgroundSitePricingOptionRepository;
import com.gocamp.reservecamping.campsite.repository.CampsiteEquipmentAllowedRepository;
import com.gocamp.reservecamping.campsite.repository.CampsitePricingAssignmentRepository;
import com.gocamp.reservecamping.campsite.repository.CampsiteRepository;
import com.gocamp.reservecamping.campsite.repository.CampsiteSurfaceTypeRepository;
import com.gocamp.reservecamping.campsite.repository.EquipmentAllowedTypeRepository;
import com.gocamp.reservecamping.campsite.repository.SiteAmperageRepository;
import com.gocamp.reservecamping.campsite.repository.SiteServiceTypeRepository;
import com.gocamp.reservecamping.campsite.repository.SiteSurfaceTypeRepository;
import com.gocamp.reservecamping.campsite.repository.SiteTypeRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class CampsiteService {

    private final CampsiteRepository repository;
    private final CampgroundRepository campgroundRepository;
    private final SiteTypeRepository siteTypeRepository;
    private final SiteServiceTypeRepository siteServiceTypeRepository;
    private final SiteAmperageRepository siteAmperageRepository;
    private final EquipmentAllowedTypeRepository equipmentAllowedTypeRepository;
    private final SiteSurfaceTypeRepository siteSurfaceTypeRepository;
    private final CampsiteEquipmentAllowedRepository campsiteEquipmentAllowedRepository;
    private final CampsiteSurfaceTypeRepository campsiteSurfaceTypeRepository;
    private final CampgroundSitePricingOptionRepository pricingOptionRepository;
    private final CampsitePricingAssignmentRepository pricingAssignmentRepository;
    private final EntityManager entityManager;

    public CampsiteService(
            CampsiteRepository repository,
            CampgroundRepository campgroundRepository,
            SiteTypeRepository siteTypeRepository,
            SiteServiceTypeRepository siteServiceTypeRepository,
            SiteAmperageRepository siteAmperageRepository,
            EquipmentAllowedTypeRepository equipmentAllowedTypeRepository,
            SiteSurfaceTypeRepository siteSurfaceTypeRepository,
            CampsiteEquipmentAllowedRepository campsiteEquipmentAllowedRepository,
            CampsiteSurfaceTypeRepository campsiteSurfaceTypeRepository,
            CampgroundSitePricingOptionRepository pricingOptionRepository,
            CampsitePricingAssignmentRepository pricingAssignmentRepository,
            EntityManager entityManager
    ) {
        this.repository = repository;
        this.campgroundRepository = campgroundRepository;
        this.siteTypeRepository = siteTypeRepository;
        this.siteServiceTypeRepository = siteServiceTypeRepository;
        this.siteAmperageRepository = siteAmperageRepository;
        this.equipmentAllowedTypeRepository = equipmentAllowedTypeRepository;
        this.siteSurfaceTypeRepository = siteSurfaceTypeRepository;
        this.campsiteEquipmentAllowedRepository = campsiteEquipmentAllowedRepository;
        this.campsiteSurfaceTypeRepository = campsiteSurfaceTypeRepository;
        this.pricingOptionRepository = pricingOptionRepository;
        this.pricingAssignmentRepository = pricingAssignmentRepository;
        this.entityManager = entityManager;
    }

    public CampsiteDetailsResponse create(CreateCampsiteRequest req) {
        Campground campground = campgroundRepository.findById(req.campgroundId())
                .orElseThrow(() -> new RuntimeException("Camping introuvable"));

        repository.findByCampgroundIdAndSiteCode(req.campgroundId(), req.siteCode())
                .ifPresent(existing -> {
                    throw new RuntimeException("Un site avec ce code existe déjà pour ce camping.");
                });

        Campsite campsite = new Campsite();
        campsite.setCampground(campground);

        applyCommonFields(
                campsite,
                req.siteCode(),
                req.siteTypeId(),
                req.siteServiceTypeId(),
                req.siteAmperageId(),
                req.hasWater(),
                req.hasElectricity(),
                req.hasSewer(),
                req.has15_20Amp(),
                req.has30Amp(),
                req.has50Amp(),
                req.widthFeet(),
                req.lengthFeet(),
                req.maxEquipmentLengthFeet(),
                req.isPullThrough(),
                req.isActive(),
                req.notes()
        );

        repository.save(campsite);
        entityManager.flush();

        replaceEquipmentAllowed(campsite.getId(), req.equipmentAllowedTypeIds());
        replaceSurfaceTypes(campsite.getId(), req.siteSurfaceTypeIds());
        replacePricingAssignment(campsite.getId(), req.pricingOptionId());

        return getDetails(campsite.getId());
    }

    public List<CampsiteResponse> getByCampground(Long campgroundId) {
        return repository.findByCampgroundIdOrderBySiteCodeAsc(campgroundId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public CampsiteDetailsResponse getDetails(Long id) {
        Campsite c = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Site introuvable"));

        List<Long> equipmentIds = campsiteEquipmentAllowedRepository.findByCampsiteId(id)
                .stream()
                .map(x -> x.getEquipmentAllowedType().getId())
                .toList();

        List<Long> surfaceIds = campsiteSurfaceTypeRepository.findByCampsiteId(id)
                .stream()
                .map(x -> x.getSiteSurfaceType().getId())
                .toList();

        var pricingAssignment = pricingAssignmentRepository.findByCampsiteId(id)
                .orElse(null);

        return new CampsiteDetailsResponse(
                c.getId(),
                c.getCampground().getId(),
                c.getSiteCode(),

                c.getSiteType() != null ? c.getSiteType().getId() : null,
                c.getSiteType() != null ? c.getSiteType().getNameFr() : null,

                c.getSiteServiceType() != null ? c.getSiteServiceType().getId() : null,
                c.getSiteServiceType() != null ? c.getSiteServiceType().getNameFr() : null,

                c.getSiteAmperage() != null ? c.getSiteAmperage().getId() : null,
                c.getSiteAmperage() != null ? c.getSiteAmperage().getNameFr() : null,

                c.isHasWater(),
                c.isHasElectricity(),
                c.isHasSewer(),

                c.isHas15_20Amp(),
                c.isHas30Amp(),
                c.isHas50Amp(),

                c.getWidthFeet(),
                c.getLengthFeet(),
                c.getMaxEquipmentLengthFeet(),
                c.isPullThrough(),
                c.isActive(),
                c.getNotes(),
                equipmentIds,
                surfaceIds,
                pricingAssignment != null ? pricingAssignment.getPricingOption().getId() : null,
                pricingAssignment != null ? pricingAssignment.getPricingOption().getName() : null
        );
    }

    public CampsiteDetailsResponse update(Long id, UpdateCampsiteRequest req) {
        Campsite campsite = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Site introuvable"));

        repository.findByCampgroundIdAndSiteCode(campsite.getCampground().getId(), req.siteCode())
                .ifPresent(existing -> {
                    if (!existing.getId().equals(id)) {
                        throw new RuntimeException("Un autre site avec ce code existe déjà pour ce camping.");
                    }
                });

        applyCommonFields(
                campsite,
                req.siteCode(),
                req.siteTypeId(),
                req.siteServiceTypeId(),
                req.siteAmperageId(),
                req.hasWater(),
                req.hasElectricity(),
                req.hasSewer(),
                req.has15_20Amp(),
                req.has30Amp(),
                req.has50Amp(),
                req.widthFeet(),
                req.lengthFeet(),
                req.maxEquipmentLengthFeet(),
                req.isPullThrough(),
                req.isActive(),
                req.notes()
        );

        repository.save(campsite);
        entityManager.flush();

        replaceEquipmentAllowed(campsite.getId(), req.equipmentAllowedTypeIds());
        replaceSurfaceTypes(campsite.getId(), req.siteSurfaceTypeIds());
        replacePricingAssignment(campsite.getId(), req.pricingOptionId());

        return getDetails(id);
    }

    public void updateMapShape(Long id, UpdateCampsiteMapShapeRequest req) {
        Campsite campsite = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Site introuvable"));

        campsite.setMapPolygonJson(req.mapPolygonJson());
        campsite.setLabelX(req.labelX());
        campsite.setLabelY(req.labelY());

        repository.save(campsite);
        entityManager.flush();
    }

    public void deleteMapShape(Long id) {
        Campsite campsite = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Site introuvable"));

        campsite.setMapPolygonJson(null);
        campsite.setLabelX(null);
        campsite.setLabelY(null);

        repository.save(campsite);
        entityManager.flush();
    }

    private void applyCommonFields(
            Campsite c,
            String siteCode,
            Long siteTypeId,
            Long siteServiceTypeId,
            Long siteAmperageId,

            Boolean hasWater,
            Boolean hasElectricity,
            Boolean hasSewer,

            Boolean has15_20Amp,
            Boolean has30Amp,
            Boolean has50Amp,

            BigDecimal widthFeet,
            BigDecimal lengthFeet,
            BigDecimal maxEquipmentLengthFeet,
            Boolean isPullThrough,
            Boolean isActive,
            String notes
    ) {
        c.setSiteCode(siteCode != null ? siteCode.trim() : null);

        c.setSiteType(siteTypeId != null
                ? siteTypeRepository.findById(siteTypeId)
                .orElseThrow(() -> new RuntimeException("Type de site invalide"))
                : null
        );

        c.setSiteServiceType(siteServiceTypeId != null
                ? siteServiceTypeRepository.findById(siteServiceTypeId)
                .orElseThrow(() -> new RuntimeException("Type de service invalide"))
                : null
        );

        c.setSiteAmperage(siteAmperageId != null
                ? siteAmperageRepository.findById(siteAmperageId)
                .orElseThrow(() -> new RuntimeException("Ampérage invalide"))
                : null
        );

        c.setHasWater(Boolean.TRUE.equals(hasWater));
        c.setHasElectricity(Boolean.TRUE.equals(hasElectricity));
        c.setHasSewer(Boolean.TRUE.equals(hasSewer));

        c.setHas15_20Amp(Boolean.TRUE.equals(has15_20Amp));
        c.setHas30Amp(Boolean.TRUE.equals(has30Amp));
        c.setHas50Amp(Boolean.TRUE.equals(has50Amp));

        c.setWidthFeet(widthFeet);
        c.setLengthFeet(lengthFeet);
        c.setMaxEquipmentLengthFeet(maxEquipmentLengthFeet);
        c.setPullThrough(Boolean.TRUE.equals(isPullThrough));
        c.setActive(isActive == null || isActive);
        c.setNotes(notes);
    }

    private void replaceEquipmentAllowed(
            Long campsiteId,
            List<Long> equipmentIds
    ) {
        campsiteEquipmentAllowedRepository.deleteAllByCampsiteId(campsiteId);
        entityManager.flush();
        entityManager.clear();

        Campsite campsite = repository.findById(campsiteId)
                .orElseThrow(() ->
                        new RuntimeException("Site introuvable après suppression des équipements autorisés."));

        List<Long> safeIds =
                equipmentIds != null ? equipmentIds : Collections.emptyList();

        for (Long equipmentId : safeIds) {
            EquipmentAllowedType type = equipmentAllowedTypeRepository.findById(equipmentId)
                    .orElseThrow(() ->
                            new RuntimeException("Type d'équipement autorisé invalide : " + equipmentId));

            CampsiteEquipmentAllowed link = new CampsiteEquipmentAllowed();
            link.setCampsite(campsite);
            link.setEquipmentAllowedType(type);

            campsiteEquipmentAllowedRepository.save(link);
        }

        entityManager.flush();
    }

    private void replaceSurfaceTypes(
            Long campsiteId,
            List<Long> surfaceIds
    ) {
        campsiteSurfaceTypeRepository.deleteAllByCampsiteId(campsiteId);
        entityManager.flush();
        entityManager.clear();

        Campsite campsite = repository.findById(campsiteId)
                .orElseThrow(() ->
                        new RuntimeException("Site introuvable après suppression des surfaces."));

        List<Long> safeIds =
                surfaceIds != null ? surfaceIds : Collections.emptyList();

        for (Long surfaceId : safeIds) {
            SiteSurfaceType type = siteSurfaceTypeRepository.findById(surfaceId)
                    .orElseThrow(() ->
                            new RuntimeException("Type de surface invalide : " + surfaceId));

            CampsiteSurfaceType link = new CampsiteSurfaceType();
            link.setCampsite(campsite);
            link.setSiteSurfaceType(type);

            campsiteSurfaceTypeRepository.save(link);
        }

        entityManager.flush();
    }

    private void replacePricingAssignment(
            Long campsiteId,
            Long pricingOptionId
    ) {
        pricingAssignmentRepository.deleteByCampsiteId(campsiteId);
        entityManager.flush();
        entityManager.clear();

        if (pricingOptionId == null) {
            return;
        }

        Campsite managedCampsite = repository.findById(campsiteId)
                .orElseThrow(() ->
                        new RuntimeException("Site introuvable lors de l'affectation tarifaire."));

        CampgroundSitePricingOption option = pricingOptionRepository
                .findByIdAndCampgroundId(
                        pricingOptionId,
                        managedCampsite.getCampground().getId()
                )
                .orElseThrow(() ->
                        new RuntimeException("Valeur de tarification invalide pour ce camping."));

        CampsitePricingAssignment assignment =
                new CampsitePricingAssignment();

        assignment.setCampsite(managedCampsite);
        assignment.setPricingOption(option);

        pricingAssignmentRepository.save(assignment);
        entityManager.flush();
    }

    private CampsiteResponse toResponse(Campsite c) {
        var pricingAssignment =
                pricingAssignmentRepository.findByCampsiteId(c.getId())
                        .orElse(null);

        return new CampsiteResponse(
                c.getId(),
                c.getSiteCode(),

                c.getSiteType() != null ? c.getSiteType().getNameFr() : null,
                c.getSiteServiceType() != null ? c.getSiteServiceType().getNameFr() : null,
                c.getSiteAmperage() != null ? c.getSiteAmperage().getNameFr() : null,

                c.isHasWater(),
                c.isHasElectricity(),
                c.isHasSewer(),

                c.isHas15_20Amp(),
                c.isHas30Amp(),
                c.isHas50Amp(),

                c.getWidthFeet(),
                c.getLengthFeet(),
                c.isPullThrough(),
                c.isActive(),
                pricingAssignment != null ? pricingAssignment.getPricingOption().getId() : null,
                pricingAssignment != null ? pricingAssignment.getPricingOption().getName() : null
        );
    }
}
