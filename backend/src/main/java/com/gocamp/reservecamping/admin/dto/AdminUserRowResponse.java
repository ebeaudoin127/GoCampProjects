// ============================================================
// Fichier : AdminUserRowResponse.java
// Dernière modification : 2026-04-16
// Auteur : ChatGPT
//
// Résumé :
// - DTO de ligne pour la liste des utilisateurs dans le module
//   Gestionnaire du site
// ============================================================

package com.gocamp.reservecamping.admin.dto;

public class AdminUserRowResponse {

    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private String city;
    private Long roleId;
    private String roleName;

    public AdminUserRowResponse(
            Long id,
            String firstname,
            String lastname,
            String email,
            String phone,
            String city,
            Long roleId,
            String roleName
    ) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phone = phone;
        this.city = city;
        this.roleId = roleId;
        this.roleName = roleName;
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

    public String getPhone() {
        return phone;
    }

    public String getCity() {
        return city;
    }

    public Long getRoleId() {
        return roleId;
    }

    public String getRoleName() {
        return roleName;
    }
}