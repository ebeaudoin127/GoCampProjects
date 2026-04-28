package com.gocamp.reservecamping.location;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "province_state")
public class ProvinceState {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    // Ex: QC, ON, TX, CA, CDMX, etc.
    @Column(nullable = false, length = 10)
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", nullable = false)
    @JsonIgnore   // 🚨 Empêche boucle JSON + erreurs Hibernate
    private Country country;

    // ===== Constructeurs =====

    public ProvinceState() {}

    public ProvinceState(String name, String code, Country country) {
        this.name = name;
        this.code = code;
        this.country = country;
    }

    // ===== Getters / Setters =====

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getCode() { return code; }
    public Country getCountry() { return country; }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setCode(String code) { this.code = code; }
    public void setCountry(Country country) { this.country = country; }
}
