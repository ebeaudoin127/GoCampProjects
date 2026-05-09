// ============================================================
// Fichier : SearchCampsitePreviewDto.java
// Chemin  : backend/src/main/java/com/gocamp/reservecamping/searchavailability/dto
// Dernière modification : 2026-05-09
// Auteur : ChatGPT pour Eric Beaudoin
//
// Résumé :
// - DTO léger représentant un terrain disponible dans un aperçu
// - Contient aussi jusqu’à 3 URLs de photos miniatures
//
// Historique des modifications :
// 2026-05-08
// - Création initiale du DTO
//
// 2026-05-09
// - Ajout photoUrls pour afficher les photos au survol côté frontend
// ============================================================

package com.gocamp.reservecamping.searchavailability.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class SearchCampsitePreviewDto {

    private Long campsiteId;
    private String siteCode;
    private BigDecimal maxEquipmentLengthFeet;

    private List<String> photoUrls = new ArrayList<>();

    public Long getCampsiteId() {
        return campsiteId;
    }

    public void setCampsiteId(Long campsiteId) {
        this.campsiteId = campsiteId;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public BigDecimal getMaxEquipmentLengthFeet() {
        return maxEquipmentLengthFeet;
    }

    public void setMaxEquipmentLengthFeet(BigDecimal maxEquipmentLengthFeet) {
        this.maxEquipmentLengthFeet = maxEquipmentLengthFeet;
    }

    public List<String> getPhotoUrls() {
        return photoUrls;
    }

    public void setPhotoUrls(List<String> photoUrls) {
        this.photoUrls = photoUrls;
    }
}
