// ============================================================
// Fichier : User.java
// Dernière modification : 2025-11-17
// Auteur : ChatGPT (corrections pour Eric Beaudoin)
// Résumé : les champs country et provinceState deviennent
//          OBLIGATOIRES (NOT NULL) lors de la création d'un compte.
// ============================================================

package com.gocamp.reservecamping.user;

import com.gocamp.reservecamping.location.Country;
import com.gocamp.reservecamping.location.ProvinceState;
import com.gocamp.reservecamping.role.Role;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- IDENTITÉ ---
    @Column(nullable = false, length = 40)
    private String firstname;

    @Column(nullable = false, length = 40)
    private String lastname;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false)
    private String password;

    // --- RÔLE ---
    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    // --- COORDONNÉES ---
    @Column(length = 20)
    private String phone;

    @Column(length = 120)
    private String address;

    @Column(length = 60)
    private String city;

    @Column(length = 15)
    private String postalCode;

    // --- GÉOGRAPHIE ---

    // 🔄 MODIFIÉ : devient obligatoire (nullable = false)
    @ManyToOne(optional = false)
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    // 🔄 MODIFIÉ : devient obligatoire (nullable = false)
    @ManyToOne(optional = false)
    @JoinColumn(name = "province_state_id", nullable = false)
    private ProvinceState provinceState;

    // --- INFO ÉQUIPEMENT ---
    @Column(length = 30)
    private String equipmentType;

    private Integer equipmentLength;

    private Boolean hasSlideOut;

    // ========= GETTERS =========
    public Long getId() { return id; }
    public String getFirstname() { return firstname; }
    public String getLastname() { return lastname; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public Role getRole() { return role; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public String getCity() { return city; }
    public String getPostalCode() { return postalCode; }
    public Country getCountry() { return country; }
    public ProvinceState getProvinceState() { return provinceState; }

    // ========= SETTERS =========
    public void setId(Long id) { this.id = id; }
    public void setFirstname(String firstname) { this.firstname = firstname; }
    public void setLastname(String lastname) { this.lastname = lastname; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setRole(Role role) { this.role = role; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setAddress(String address) { this.address = address; }
    public void setCity(String city) { this.city = city; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }
    public void setCountry(Country country) { this.country = country; }
    public void setProvinceState(ProvinceState provinceState) { this.provinceState = provinceState; }
  }
