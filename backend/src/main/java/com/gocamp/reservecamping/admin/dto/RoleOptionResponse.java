// ============================================================
// Fichier : RoleOptionResponse.java
// Dernière modification : 2026-04-16
// Auteur : ChatGPT
//
// Résumé :
// - DTO simple pour alimenter la liste déroulante des rôles
// ============================================================

package com.gocamp.reservecamping.admin.dto;

public class RoleOptionResponse {

    private Long id;
    private String name;

    public RoleOptionResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}