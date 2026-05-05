// ============================================================
// Fichier : CampgroundService.java
// Dernière modification : 2026-05-04
// Auteur : ChatGPT
//
// Résumé :
// - Service backend du module campings
// - Permet de créer, lister, consulter et modifier un camping
// - Valide le pays et la province/état
// - Valide les périodes de réservation
// - Transforme les entités en DTO
// ============================================================

package com.gocamp.reservecamping.campground;

import com.gocamp.reservecamping.campground.dto.CampgroundDetailsResponse;
import com.gocamp.reservecamping.campground.dto.CampgroundReservationPeriodRequest;
import com.gocamp.reservecamping.campground.dto.CampgroundReservationPeriodResponse;
import com.gocamp.reservecamping.campground.dto.CampgroundRowResponse;
import com.gocamp.reservecamping.campground.dto.CreateCampgroundRequest;
import com.gocamp.reservecamping.campground.dto.UpdateCampgroundRequest;
import com.gocamp.reservecamping.campground.model.Campground;
import com.gocamp.reservecamping.campground.model.CampgroundReservationPeriod;
import com.gocamp.reservecamping.campground.repository.CampgroundRepository;
import com.gocamp.reservecamping.location.Country;
import com.gocamp.reservecamping.location.CountryRepository;
import com.gocamp.reservecamping.location.ProvinceState;
import com.gocamp.reservecamping.location.ProvinceStateRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
public class CampgroundService {

    private final CampgroundRepository campgroundRepo;
    private final CountryRepository countryRepo;
    private final ProvinceStateRepository provinceStateRepo;

    public CampgroundService(
            CampgroundRepository campgroundRepo,
            CountryRepository countryRepo,
            ProvinceStateRepository provinceStateRepo
    ) {
        this.campgroundRepo = campgroundRepo;
        this.countryRepo = countryRepo;
        this.provinceStateRepo = provinceStateRepo;
    }

    // ============================================================
    // LISTE
    // ============================================================
    public List<CampgroundRowResponse> getAllCampgrounds() {
        return campgroundRepo.findAllByOrderByNameAsc()
                .stream()
                .map(this::toRowResponse)
                .toList();
    }

    // ============================================================
    // DÉTAIL
    // ============================================================
    public CampgroundDetailsResponse getCampgroundById(Long id) {
        Campground campground = campgroundRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Camping introuvable."));

        return toDetailsResponse(campground);
    }

    // ============================================================
    // CRÉATION
    // ============================================================
    @Transactional
    public CampgroundDetailsResponse createCampground(CreateCampgroundRequest req) {
        validateRequiredFieldsForCreate(req);

        Country country = countryRepo.findById(req.countryId())
                .orElseThrow(() -> new RuntimeException("Pays non valide."));

        ProvinceState provinceState = provinceStateRepo.findById(req.provinceStateId())
                .orElseThrow(() -> new RuntimeException("Province/État non valide."));

        validateProvinceBelongsToCountry(country, provinceState);
        validateNoDuplicateNameAndCity(req.name(), req.city(), null);
        validateSiteCounts(
                req.totalSites(),
                req.sites3Services(),
                req.sites2Services(),
                req.sites1Service(),
                req.sitesNoService()
        );
        validateShadePercentage(req.shadePercentage());
        validateReservationPeriods(req.reservationPeriods());

        Campground campground = new Campground();
        applyCreateRequestToEntity(campground, req, country, provinceState);
        replaceReservationPeriods(campground, req.reservationPeriods());

        campgroundRepo.save(campground);

        return toDetailsResponse(campground);
    }

    // ============================================================
    // MODIFICATION
    // ============================================================
    @Transactional
    public CampgroundDetailsResponse updateCampground(Long id, UpdateCampgroundRequest req) {
        Campground campground = campgroundRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Camping introuvable."));

        validateRequiredFieldsForUpdate(req);

        Country country = countryRepo.findById(req.countryId())
                .orElseThrow(() -> new RuntimeException("Pays non valide."));

        ProvinceState provinceState = provinceStateRepo.findById(req.provinceStateId())
                .orElseThrow(() -> new RuntimeException("Province/État non valide."));

        validateProvinceBelongsToCountry(country, provinceState);
        validateNoDuplicateNameAndCity(req.name(), req.city(), id);
        validateSiteCounts(
                req.totalSites(),
                req.sites3Services(),
                req.sites2Services(),
                req.sites1Service(),
                req.sitesNoService()
        );
        validateShadePercentage(req.shadePercentage());
        validateReservationPeriods(req.reservationPeriods());

        applyUpdateRequestToEntity(campground, req, country, provinceState);
        replaceReservationPeriods(campground, req.reservationPeriods());

        campgroundRepo.save(campground);

        return toDetailsResponse(campground);
    }

    // ============================================================
    // VALIDATIONS
    // ============================================================
    private void validateRequiredFieldsForCreate(CreateCampgroundRequest req) {
        if (req == null) {
            throw new RuntimeException("La requête est obligatoire.");
        }

        validateRequiredCommonFields(
                req.name(),
                req.addressLine1(),
                req.city(),
                req.countryId(),
                req.provinceStateId()
        );
    }

    private void validateRequiredFieldsForUpdate(UpdateCampgroundRequest req) {
        if (req == null) {
            throw new RuntimeException("La requête est obligatoire.");
        }

        validateRequiredCommonFields(
                req.name(),
                req.addressLine1(),
                req.city(),
                req.countryId(),
                req.provinceStateId()
        );
    }

    private void validateRequiredCommonFields(
            String name,
            String addressLine1,
            String city,
            Long countryId,
            Long provinceStateId
    ) {
        if (isBlank(name)) {
            throw new RuntimeException("Le nom du camping est obligatoire.");
        }

        if (isBlank(addressLine1)) {
            throw new RuntimeException("L'adresse principale est obligatoire.");
        }

        if (isBlank(city)) {
            throw new RuntimeException("La ville est obligatoire.");
        }

        if (countryId == null) {
            throw new RuntimeException("Le pays est obligatoire.");
        }

        if (provinceStateId == null) {
            throw new RuntimeException("La province/état est obligatoire.");
        }
    }

    private void validateProvinceBelongsToCountry(Country country, ProvinceState provinceState) {
        if (provinceState.getCountry() == null || provinceState.getCountry().getId() == null) {
            throw new RuntimeException("La province/état n'est pas reliée à un pays valide.");
        }

        if (!provinceState.getCountry().getId().equals(country.getId())) {
            throw new RuntimeException("Cette province/état n'appartient pas au pays sélectionné.");
        }
    }

    private void validateNoDuplicateNameAndCity(String name, String city, Long currentCampgroundId) {
        campgroundRepo.findByNameAndCity(name.trim(), city.trim())
                .ifPresent(existing -> {
                    if (currentCampgroundId == null || !existing.getId().equals(currentCampgroundId)) {
                        throw new RuntimeException("Un camping avec ce nom existe déjà dans cette ville.");
                    }
                });
    }

    private void validateSiteCounts(
            Integer totalSites,
            Integer sites3Services,
            Integer sites2Services,
            Integer sites1Service,
            Integer sitesNoService
    ) {
        int total = safeInt(totalSites);
        int s3 = safeInt(sites3Services);
        int s2 = safeInt(sites2Services);
        int s1 = safeInt(sites1Service);
        int s0 = safeInt(sitesNoService);

        if (total < 0 || s3 < 0 || s2 < 0 || s1 < 0 || s0 < 0) {
            throw new RuntimeException("Les nombres de sites ne peuvent pas être négatifs.");
        }

        if ((s3 + s2 + s1 + s0) > total && total > 0) {
            throw new RuntimeException("La somme des types de sites ne peut pas dépasser le nombre total de sites.");
        }
    }

    private void validateShadePercentage(Integer shadePercentage) {
        if (shadePercentage == null) {
            return;
        }

        if (shadePercentage < 0 || shadePercentage > 100) {
            throw new RuntimeException("Le pourcentage d'ombrage doit être entre 0 et 100.");
        }
    }

    private void validateReservationPeriods(List<CampgroundReservationPeriodRequest> periods) {
        if (periods == null || periods.isEmpty()) {
            return;
        }

        List<CampgroundReservationPeriodRequest> normalized = periods.stream()
                .filter(period -> period != null)
                .sorted(Comparator.comparing(
                        CampgroundReservationPeriodRequest::startDate,
                        Comparator.nullsLast(Comparator.naturalOrder())
                ))
                .toList();

        for (int i = 0; i < normalized.size(); i++) {
            CampgroundReservationPeriodRequest current = normalized.get(i);

            if (current.startDate() == null) {
                throw new RuntimeException("La date de début d'une période de réservation est obligatoire.");
            }

            if (current.endDate() == null) {
                throw new RuntimeException("La date de fin d'une période de réservation est obligatoire.");
            }

            if (current.endDate().isBefore(current.startDate())) {
                throw new RuntimeException("La date de fin d'une période de réservation doit être après ou égale à la date de début.");
            }

            for (int j = i + 1; j < normalized.size(); j++) {
                CampgroundReservationPeriodRequest next = normalized.get(j);

                if (next.startDate() == null || next.endDate() == null) {
                    continue;
                }

                boolean overlaps = !current.endDate().isBefore(next.startDate())
                        && !next.endDate().isBefore(current.startDate());

                if (overlaps) {
                    throw new RuntimeException(
                            "Les périodes de réservation ne doivent pas se chevaucher : "
                                    + current.startDate()
                                    + " au "
                                    + current.endDate()
                                    + " chevauche "
                                    + next.startDate()
                                    + " au "
                                    + next.endDate()
                                    + "."
                    );
                }
            }
        }
    }

    // ============================================================
    // MAPPINGS REQUÊTES -> ENTITÉ
    // ============================================================
    private void applyCreateRequestToEntity(
            Campground campground,
            CreateCampgroundRequest req,
            Country country,
            ProvinceState provinceState
    ) {
        campground.setName(req.name().trim());
        campground.setShortDescription(trimToNull(req.shortDescription()));
        campground.setLongDescription(trimToNull(req.longDescription()));
        campground.setAddressLine1(req.addressLine1().trim());
        campground.setAddressLine2(trimToNull(req.addressLine2()));
        campground.setCity(req.city().trim());
        campground.setProvinceState(provinceState);
        campground.setCountry(country);
        campground.setPostalCode(trimToNull(req.postalCode()));
        campground.setPhoneMain(trimToNull(req.phoneMain()));
        campground.setPhoneSecondary(trimToNull(req.phoneSecondary()));
        campground.setEmail(trimToNull(req.email()));
        campground.setWebsite(trimToNull(req.website()));
        campground.setGpsLatitude(req.gpsLatitude());
        campground.setGpsLongitude(req.gpsLongitude());
        campground.setOpeningDate(req.openingDate());
        campground.setClosingDate(req.closingDate());
        campground.setCheckInTime(req.checkInTime());
        campground.setCheckOutTime(req.checkOutTime());
        campground.setTotalSites(safeDefault(req.totalSites()));
        campground.setSites3Services(safeDefault(req.sites3Services()));
        campground.setSites2Services(safeDefault(req.sites2Services()));
        campground.setSites1Service(safeDefault(req.sites1Service()));
        campground.setSitesNoService(safeDefault(req.sitesNoService()));
        campground.setTravelerSitesCount(safeDefault(req.travelerSitesCount()));
        campground.setShadePercentage(req.shadePercentage());
        campground.setHasWifi(req.hasWifi() != null ? req.hasWifi() : false);
        campground.setIsWinterCamping(req.isWinterCamping() != null ? req.isWinterCamping() : false);
        campground.setIsActive(req.isActive() != null ? req.isActive() : true);
    }

    private void applyUpdateRequestToEntity(
            Campground campground,
            UpdateCampgroundRequest req,
            Country country,
            ProvinceState provinceState
    ) {
        campground.setName(req.name().trim());
        campground.setShortDescription(trimToNull(req.shortDescription()));
        campground.setLongDescription(trimToNull(req.longDescription()));
        campground.setAddressLine1(req.addressLine1().trim());
        campground.setAddressLine2(trimToNull(req.addressLine2()));
        campground.setCity(req.city().trim());
        campground.setProvinceState(provinceState);
        campground.setCountry(country);
        campground.setPostalCode(trimToNull(req.postalCode()));
        campground.setPhoneMain(trimToNull(req.phoneMain()));
        campground.setPhoneSecondary(trimToNull(req.phoneSecondary()));
        campground.setEmail(trimToNull(req.email()));
        campground.setWebsite(trimToNull(req.website()));
        campground.setGpsLatitude(req.gpsLatitude());
        campground.setGpsLongitude(req.gpsLongitude());
        campground.setOpeningDate(req.openingDate());
        campground.setClosingDate(req.closingDate());
        campground.setCheckInTime(req.checkInTime());
        campground.setCheckOutTime(req.checkOutTime());
        campground.setTotalSites(safeDefault(req.totalSites()));
        campground.setSites3Services(safeDefault(req.sites3Services()));
        campground.setSites2Services(safeDefault(req.sites2Services()));
        campground.setSites1Service(safeDefault(req.sites1Service()));
        campground.setSitesNoService(safeDefault(req.sitesNoService()));
        campground.setTravelerSitesCount(safeDefault(req.travelerSitesCount()));
        campground.setShadePercentage(req.shadePercentage());
        campground.setHasWifi(req.hasWifi() != null ? req.hasWifi() : false);
        campground.setIsWinterCamping(req.isWinterCamping() != null ? req.isWinterCamping() : false);
        campground.setIsActive(req.isActive() != null ? req.isActive() : true);
    }

    private void replaceReservationPeriods(
            Campground campground,
            List<CampgroundReservationPeriodRequest> periodRequests
    ) {
        campground.getReservationPeriods().clear();

        if (periodRequests == null || periodRequests.isEmpty()) {
            return;
        }

        List<CampgroundReservationPeriod> periods = periodRequests.stream()
                .filter(req -> req != null)
                .sorted(Comparator.comparing(CampgroundReservationPeriodRequest::startDate))
                .map(req -> {
                    CampgroundReservationPeriod period = new CampgroundReservationPeriod();
                    period.setCampground(campground);
                    period.setStartDate(req.startDate());
                    period.setEndDate(req.endDate());
                    period.setActive(req.active() != null ? req.active() : true);
                    return period;
                })
                .toList();

        campground.getReservationPeriods().addAll(periods);
    }

    // ============================================================
    // MAPPINGS ENTITÉ -> DTO
    // ============================================================
    private CampgroundRowResponse toRowResponse(Campground c) {
        return new CampgroundRowResponse(
                c.getId(),
                c.getName(),
                c.getCity(),
                c.getCountry() != null ? c.getCountry().getId() : null,
                c.getCountry() != null ? c.getCountry().getName() : null,
                c.getProvinceState() != null ? c.getProvinceState().getId() : null,
                c.getProvinceState() != null ? c.getProvinceState().getName() : null,
                c.getEmail(),
                c.getPhoneMain(),
                c.getTotalSites(),
                c.getHasWifi(),
                c.getIsWinterCamping(),
                c.getIsActive()
        );
    }

    private CampgroundDetailsResponse toDetailsResponse(Campground c) {
        return new CampgroundDetailsResponse(
                c.getId(),
                c.getName(),
                c.getShortDescription(),
                c.getLongDescription(),
                c.getAddressLine1(),
                c.getAddressLine2(),
                c.getCity(),
                c.getProvinceState() != null ? c.getProvinceState().getId() : null,
                c.getProvinceState() != null ? c.getProvinceState().getName() : null,
                c.getCountry() != null ? c.getCountry().getId() : null,
                c.getCountry() != null ? c.getCountry().getName() : null,
                c.getPostalCode(),
                c.getPhoneMain(),
                c.getPhoneSecondary(),
                c.getEmail(),
                c.getWebsite(),
                c.getGpsLatitude(),
                c.getGpsLongitude(),
                c.getOpeningDate(),
                c.getClosingDate(),
                c.getCheckInTime(),
                c.getCheckOutTime(),
                c.getTotalSites(),
                c.getSites3Services(),
                c.getSites2Services(),
                c.getSites1Service(),
                c.getSitesNoService(),
                c.getTravelerSitesCount(),
                c.getShadePercentage(),
                c.getHasWifi(),
                c.getIsWinterCamping(),
                c.getIsActive(),
                c.getCreatedAt(),
                c.getUpdatedAt(),
                toReservationPeriodResponses(c)
        );
    }

    private List<CampgroundReservationPeriodResponse> toReservationPeriodResponses(Campground campground) {
        if (campground.getReservationPeriods() == null) {
            return List.of();
        }

        return campground.getReservationPeriods()
                .stream()
                .sorted(Comparator.comparing(CampgroundReservationPeriod::getStartDate))
                .map(period -> new CampgroundReservationPeriodResponse(
                        period.getId(),
                        period.getStartDate(),
                        period.getEndDate(),
                        period.getActive()
                ))
                .toList();
    }

    // ============================================================
    // UTILS
    // ============================================================
    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private String trimToNull(String value) {
        if (value == null) return null;

        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private int safeInt(Integer value) {
        return value == null ? 0 : value;
    }

    private Integer safeDefault(Integer value) {
        return value == null ? 0 : value;
    }
}