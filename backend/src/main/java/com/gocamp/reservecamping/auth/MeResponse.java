// ============================================================
// Fichier : MeResponse.java
// Chemin  : backend/src/main/java/com/gocamp/reservecamping/auth
// Dernière modification : 2026-05-14
// Auteur : ChatGPT pour Eric Beaudoin
//
// Résumé :
// - DTO simple pour le frontend
// - role est une STRING (SUPER_ADMIN, CAMPING_ADMIN, etc.)
// - Ajoute les coordonnées complètes du client pour la page
//   de confirmation de réservation
//
// Historique des modifications :
// 2026-04-16
// - Création du DTO /auth/me
// - Retour du rôle en String
//
// 2026-05-14
// - Ajout phone
// - Ajout address
// - Ajout city
// - Ajout postalCode
// - Ajout countryName
// - Ajout provinceStateName
// ============================================================

package com.gocamp.reservecamping.auth;

public class MeResponse {

    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String role;

    private String phone;
    private String address;
    private String city;
    private String postalCode;
    private String countryName;
    private String provinceStateName;

    public MeResponse(
            Long id,
            String firstname,
            String lastname,
            String email,
            String role,
            String phone,
            String address,
            String city,
            String postalCode,
            String countryName,
            String provinceStateName
    ) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.role = role;
        this.phone = phone;
        this.address = address;
        this.city = city;
        this.postalCode = postalCode;
        this.countryName = countryName;
        this.provinceStateName = provinceStateName;
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

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public String getProvinceStateName() {
        return provinceStateName;
    }
}
