// ============================================================
// Fichier : ReferenceOptionResponse.java
// Dernière modification : 2026-04-17
// Auteur : ChatGPT
//
// Résumé :
// - DTO simple pour afficher les listes de référence
// ============================================================

package com.gocamp.reservecamping.campground.dto;

public class ReferenceOptionResponse {

    private Long id;
    private String code;
    private String nameFr;
    private String category;

    public ReferenceOptionResponse(Long id, String code, String nameFr, String category) {
        this.id = id;
        this.code = code;
        this.nameFr = nameFr;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getNameFr() {
        return nameFr;
    }

    public String getCategory() {
        return category;
    }
}