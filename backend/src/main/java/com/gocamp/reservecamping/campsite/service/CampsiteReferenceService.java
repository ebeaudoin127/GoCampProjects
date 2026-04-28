package com.gocamp.reservecamping.campsite.service;

import com.gocamp.reservecamping.campsite.dto.CampsiteReferenceOptionResponse;
import com.gocamp.reservecamping.campsite.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CampsiteReferenceService {

    private final SiteTypeRepository siteTypeRepository;
    private final SiteServiceTypeRepository siteServiceTypeRepository;
    private final SiteAmperageRepository siteAmperageRepository;
    private final EquipmentAllowedTypeRepository equipmentAllowedTypeRepository;
    private final SiteSurfaceTypeRepository siteSurfaceTypeRepository;

    public CampsiteReferenceService(
            SiteTypeRepository siteTypeRepository,
            SiteServiceTypeRepository siteServiceTypeRepository,
            SiteAmperageRepository siteAmperageRepository,
            EquipmentAllowedTypeRepository equipmentAllowedTypeRepository,
            SiteSurfaceTypeRepository siteSurfaceTypeRepository
    ) {
        this.siteTypeRepository = siteTypeRepository;
        this.siteServiceTypeRepository = siteServiceTypeRepository;
        this.siteAmperageRepository = siteAmperageRepository;
        this.equipmentAllowedTypeRepository = equipmentAllowedTypeRepository;
        this.siteSurfaceTypeRepository = siteSurfaceTypeRepository;
    }

    public List<CampsiteReferenceOptionResponse> getSiteTypes() {
        return siteTypeRepository.findByIsActiveTrueOrderByDisplayOrderAscNameFrAsc()
                .stream()
                .map(x -> new CampsiteReferenceOptionResponse(x.getId(), x.getCode(), x.getNameFr()))
                .toList();
    }

    public List<CampsiteReferenceOptionResponse> getSiteServiceTypes() {
        return siteServiceTypeRepository.findByIsActiveTrueOrderByDisplayOrderAscNameFrAsc()
                .stream()
                .map(x -> new CampsiteReferenceOptionResponse(x.getId(), x.getCode(), x.getNameFr()))
                .toList();
    }

    public List<CampsiteReferenceOptionResponse> getSiteAmperages() {
        return siteAmperageRepository.findByIsActiveTrueOrderByDisplayOrderAscNameFrAsc()
                .stream()
                .map(x -> new CampsiteReferenceOptionResponse(x.getId(), x.getCode(), x.getNameFr()))
                .toList();
    }

    public List<CampsiteReferenceOptionResponse> getEquipmentAllowedTypes() {
        return equipmentAllowedTypeRepository.findByIsActiveTrueOrderByDisplayOrderAscNameFrAsc()
                .stream()
                .map(x -> new CampsiteReferenceOptionResponse(x.getId(), x.getCode(), x.getNameFr()))
                .toList();
    }

    public List<CampsiteReferenceOptionResponse> getSiteSurfaceTypes() {
        return siteSurfaceTypeRepository.findByIsActiveTrueOrderByDisplayOrderAscNameFrAsc()
                .stream()
                .map(x -> new CampsiteReferenceOptionResponse(x.getId(), x.getCode(), x.getNameFr()))
                .toList();
    }
}
