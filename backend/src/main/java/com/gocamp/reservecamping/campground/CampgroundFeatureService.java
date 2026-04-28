// ============================================================
// Fichier : CampgroundFeatureService.java
// Dernière modification : 2026-04-17
// Auteur : ChatGPT
//
// Résumé :
// - Gère les références services / activités / hébergements
// - Gère les associations d’un camping
// - CORRECTION : suppression fiable + flush avant réinsertion
// ============================================================

package com.gocamp.reservecamping.campground;

import com.gocamp.reservecamping.campground.dto.CampgroundFeaturesResponse;
import com.gocamp.reservecamping.campground.dto.ReferenceOptionResponse;
import com.gocamp.reservecamping.campground.dto.UpdateCampgroundFeaturesRequest;
import com.gocamp.reservecamping.campground.model.AccommodationType;
import com.gocamp.reservecamping.campground.model.Activity;
import com.gocamp.reservecamping.campground.model.Campground;
import com.gocamp.reservecamping.campground.model.CampgroundAccommodationType;
import com.gocamp.reservecamping.campground.model.CampgroundActivity;
import com.gocamp.reservecamping.campground.model.ServiceEntity;
import com.gocamp.reservecamping.campground.repository.AccommodationTypeRepository;
import com.gocamp.reservecamping.campground.repository.ActivityRepository;
import com.gocamp.reservecamping.campground.repository.CampgroundAccommodationTypeRepository;
import com.gocamp.reservecamping.campground.repository.CampgroundActivityRepository;
import com.gocamp.reservecamping.campground.repository.CampgroundRepository;
import com.gocamp.reservecamping.campground.repository.CampgroundServiceRepository;
import com.gocamp.reservecamping.campground.repository.ServiceEntityRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class CampgroundFeatureService {

    private final CampgroundRepository campgroundRepo;
    private final ServiceEntityRepository serviceEntityRepo;
    private final ActivityRepository activityRepo;
    private final AccommodationTypeRepository accommodationTypeRepo;
    private final CampgroundServiceRepository campgroundServiceRepo;
    private final CampgroundActivityRepository campgroundActivityRepo;
    private final CampgroundAccommodationTypeRepository campgroundAccommodationTypeRepo;
    private final EntityManager entityManager;

    public CampgroundFeatureService(
            CampgroundRepository campgroundRepo,
            ServiceEntityRepository serviceEntityRepo,
            ActivityRepository activityRepo,
            AccommodationTypeRepository accommodationTypeRepo,
            CampgroundServiceRepository campgroundServiceRepo,
            CampgroundActivityRepository campgroundActivityRepo,
            CampgroundAccommodationTypeRepository campgroundAccommodationTypeRepo,
            EntityManager entityManager
    ) {
        this.campgroundRepo = campgroundRepo;
        this.serviceEntityRepo = serviceEntityRepo;
        this.activityRepo = activityRepo;
        this.accommodationTypeRepo = accommodationTypeRepo;
        this.campgroundServiceRepo = campgroundServiceRepo;
        this.campgroundActivityRepo = campgroundActivityRepo;
        this.campgroundAccommodationTypeRepo = campgroundAccommodationTypeRepo;
        this.entityManager = entityManager;
    }

    public List<ReferenceOptionResponse> getServices() {
        return serviceEntityRepo.findByIsActiveTrueOrderByDisplayOrderAscNameFrAsc()
                .stream()
                .map(s -> new ReferenceOptionResponse(
                        s.getId(),
                        s.getCode(),
                        s.getNameFr(),
                        null
                ))
                .toList();
    }

    public List<ReferenceOptionResponse> getActivities() {
        return activityRepo.findByIsActiveTrueOrderByDisplayOrderAscNameFrAsc()
                .stream()
                .map(a -> new ReferenceOptionResponse(
                        a.getId(),
                        a.getCode(),
                        a.getNameFr(),
                        null
                ))
                .toList();
    }

    public List<ReferenceOptionResponse> getAccommodationTypes() {
        return accommodationTypeRepo.findByIsActiveTrueOrderByDisplayOrderAscNameFrAsc()
                .stream()
                .map(a -> new ReferenceOptionResponse(
                        a.getId(),
                        a.getCode(),
                        a.getNameFr(),
                        a.getCategory()
                ))
                .toList();
    }

    public CampgroundFeaturesResponse getCampgroundFeatures(Long campgroundId) {
        campgroundRepo.findById(campgroundId)
                .orElseThrow(() -> new RuntimeException("Camping introuvable."));

        List<Long> serviceIds = campgroundServiceRepo.findByCampgroundId(campgroundId)
                .stream()
                .map(cs -> cs.getService().getId())
                .toList();

        List<Long> activityIds = campgroundActivityRepo.findByCampgroundId(campgroundId)
                .stream()
                .map(ca -> ca.getActivity().getId())
                .toList();

        List<Long> accommodationTypeIds = campgroundAccommodationTypeRepo.findByCampgroundId(campgroundId)
                .stream()
                .map(cat -> cat.getAccommodationType().getId())
                .toList();

        return new CampgroundFeaturesResponse(
                campgroundId,
                serviceIds,
                activityIds,
                accommodationTypeIds
        );
    }

    public CampgroundFeaturesResponse updateCampgroundFeatures(Long campgroundId, UpdateCampgroundFeaturesRequest req) {
        Campground campground = campgroundRepo.findById(campgroundId)
                .orElseThrow(() -> new RuntimeException("Camping introuvable."));

        List<Long> serviceIds = req.serviceIds() != null ? req.serviceIds() : Collections.emptyList();
        List<Long> activityIds = req.activityIds() != null ? req.activityIds() : Collections.emptyList();
        List<Long> accommodationTypeIds = req.accommodationTypeIds() != null ? req.accommodationTypeIds() : Collections.emptyList();

        campgroundServiceRepo.deleteAllByCampgroundId(campgroundId);
        campgroundActivityRepo.deleteAllByCampgroundId(campgroundId);
        campgroundAccommodationTypeRepo.deleteAllByCampgroundId(campgroundId);

        entityManager.flush();
        entityManager.clear();

        campground = campgroundRepo.findById(campgroundId)
                .orElseThrow(() -> new RuntimeException("Camping introuvable après suppression des associations."));

        for (Long serviceId : serviceIds) {
            ServiceEntity service = serviceEntityRepo.findById(serviceId)
                    .orElseThrow(() -> new RuntimeException("Service introuvable : " + serviceId));

            com.gocamp.reservecamping.campground.model.CampgroundService association =
                    new com.gocamp.reservecamping.campground.model.CampgroundService();
            association.setCampground(campground);
            association.setService(service);
            association.setIsIncluded(true);

            campgroundServiceRepo.save(association);
        }

        for (Long activityId : activityIds) {
            Activity activity = activityRepo.findById(activityId)
                    .orElseThrow(() -> new RuntimeException("Activité introuvable : " + activityId));

            CampgroundActivity association = new CampgroundActivity();
            association.setCampground(campground);
            association.setActivity(activity);
            association.setIsIncluded(true);

            campgroundActivityRepo.save(association);
        }

        for (Long accommodationTypeId : accommodationTypeIds) {
            AccommodationType accommodationType = accommodationTypeRepo.findById(accommodationTypeId)
                    .orElseThrow(() -> new RuntimeException("Type d'hébergement introuvable : " + accommodationTypeId));

            CampgroundAccommodationType association = new CampgroundAccommodationType();
            association.setCampground(campground);
            association.setAccommodationType(accommodationType);
            association.setIsAvailable(true);

            campgroundAccommodationTypeRepo.save(association);
        }

        entityManager.flush();

        return getCampgroundFeatures(campgroundId);
    }
}