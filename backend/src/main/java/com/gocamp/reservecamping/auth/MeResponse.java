// ============================================================
// Fichier : MeResponse.java
// Modifié : 2026-04-16
//
// Résumé :
// - DTO simple pour le frontend
// - role est une STRING (SUPER_ADMIN, CAMPING_ADMIN, etc.)
// ============================================================

package com.gocamp.reservecamping.auth;

public class MeResponse {

    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String role;

    public MeResponse(Long id, String firstname, String lastname, String email, String role) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}