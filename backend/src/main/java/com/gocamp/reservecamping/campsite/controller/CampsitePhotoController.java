// ============================================================
// Fichier : CampsitePhotoController.java
// Chemin : backend/src/main/java/com/gocamp/reservecamping/campsite/controller
// Dernière modification : 2026-04-18
//
// Résumé :
// - Contrôleur REST pour les photos de site
// - Upload, listage, suppression, photo principale
// ============================================================

package com.gocamp.reservecamping.campsite.controller;

import com.gocamp.reservecamping.campsite.service.CampsitePhotoService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class CampsitePhotoController {

    private final CampsitePhotoService campsitePhotoService;

    public CampsitePhotoController(CampsitePhotoService campsitePhotoService) {
        this.campsitePhotoService = campsitePhotoService;
    }

    @GetMapping("/campsites/{campsiteId}/photos")
    public Object getByCampsite(@PathVariable Long campsiteId) {
        return campsitePhotoService.getByCampsite(campsiteId);
    }

    @PostMapping(value = "/campsites/{campsiteId}/photos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Object uploadPhoto(
            @PathVariable Long campsiteId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "captionFr", required = false) String captionFr,
            @RequestParam(value = "captionEn", required = false) String captionEn,
            @RequestParam(value = "isPrimary", required = false) Boolean isPrimary
    ) {
        return campsitePhotoService.uploadPhoto(campsiteId, file, captionFr, captionEn, isPrimary);
    }

    @DeleteMapping("/campsite-photos/{photoId}")
    public Object deletePhoto(@PathVariable Long photoId) {
        campsitePhotoService.deletePhoto(photoId);
        return java.util.Map.of("success", true);
    }

    @PutMapping("/campsite-photos/{photoId}/primary")
    public Object setPrimary(@PathVariable Long photoId) {
        campsitePhotoService.setPrimary(photoId);
        return java.util.Map.of("success", true);
    }
}