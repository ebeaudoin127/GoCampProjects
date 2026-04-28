// ============================================================
// Fichier : backend/src/main/java/com/gocamp/reservecamping/campsite/service/CampsiteUnavailabilityService.java
// Dernière modification : 2026-04-20
//
// Résumé :
// - Service métier pour gérer les périodes d’indisponibilité des sites
// - Permet création, lecture, modification et suppression
// ============================================================

package com.gocamp.reservecamping.campsite.service;

import com.gocamp.reservecamping.campsite.dto.CampsiteUnavailabilityResponse;
import com.gocamp.reservecamping.campsite.dto.CreateCampsiteUnavailabilityRequest;
import com.gocamp.reservecamping.campsite.dto.UpdateCampsiteUnavailabilityRequest;
import com.gocamp.reservecamping.campsite.model.Campsite;
import com.gocamp.reservecamping.campsite.model.CampsiteUnavailability;
import com.gocamp.reservecamping.campsite.repository.CampsiteRepository;
import com.gocamp.reservecamping.campsite.repository.CampsiteUnavailabilityRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CampsiteUnavailabilityService {

    private final CampsiteRepository campsiteRepository;
    private final CampsiteUnavailabilityRepository repository;

    public CampsiteUnavailabilityService(
            CampsiteRepository campsiteRepository,
            CampsiteUnavailabilityRepository repository
    ) {
        this.campsiteRepository = campsiteRepository;
        this.repository = repository;
    }

    public List<CampsiteUnavailabilityResponse> getByCampsite(Long campsiteId) {
        return repository.findByCampsiteIdOrderByStartDateAscEndDateAsc(campsiteId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public CampsiteUnavailabilityResponse create(CreateCampsiteUnavailabilityRequest req) {
        if (req.campsiteId() == null) {
            throw new RuntimeException("Le site est requis.");
        }

        validateDates(req.startDate(), req.endDate());
        validateReason(req.reason());

        Campsite campsite = campsiteRepository.findById(req.campsiteId())
                .orElseThrow(() -> new RuntimeException("Site introuvable."));

        CampsiteUnavailability entity = new CampsiteUnavailability();
        entity.setCampsite(campsite);
        entity.setStartDate(req.startDate());
        entity.setEndDate(req.endDate());
        entity.setReason(req.reason().trim());
        entity.setBlocking(req.isBlocking() == null || req.isBlocking());
        entity.setNotes(cleanNotes(req.notes()));

        repository.save(entity);

        return toResponse(entity);
    }

    public CampsiteUnavailabilityResponse update(Long id, UpdateCampsiteUnavailabilityRequest req) {
        CampsiteUnavailability entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Période d’indisponibilité introuvable."));

        validateDates(req.startDate(), req.endDate());
        validateReason(req.reason());

        entity.setStartDate(req.startDate());
        entity.setEndDate(req.endDate());
        entity.setReason(req.reason().trim());
        entity.setBlocking(req.isBlocking() == null || req.isBlocking());
        entity.setNotes(cleanNotes(req.notes()));

        repository.save(entity);

        return toResponse(entity);
    }

    public void delete(Long id) {
        CampsiteUnavailability entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Période d’indisponibilité introuvable."));

        repository.delete(entity);
    }

    private void validateDates(java.time.LocalDate startDate, java.time.LocalDate endDate) {
        if (startDate == null) {
            throw new RuntimeException("La date de début est requise.");
        }
        if (endDate == null) {
            throw new RuntimeException("La date de fin est requise.");
        }
        if (endDate.isBefore(startDate)) {
            throw new RuntimeException("La date de fin doit être égale ou postérieure à la date de début.");
        }
    }

    private void validateReason(String reason) {
        if (reason == null || reason.isBlank()) {
            throw new RuntimeException("La raison est requise.");
        }
        if (reason.trim().length() > 150) {
            throw new RuntimeException("La raison ne peut pas dépasser 150 caractères.");
        }
    }

    private String cleanNotes(String notes) {
        if (notes == null || notes.isBlank()) {
            return null;
        }
        return notes.trim();
    }

    private CampsiteUnavailabilityResponse toResponse(CampsiteUnavailability entity) {
        return new CampsiteUnavailabilityResponse(
                entity.getId(),
                entity.getCampsite().getId(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getReason(),
                entity.isBlocking(),
                entity.getNotes(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
