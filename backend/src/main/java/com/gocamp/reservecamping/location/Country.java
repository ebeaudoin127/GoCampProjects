package com.gocamp.reservecamping.location;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "country")
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    // Exemple: CA, US, MX
    @Column(nullable = false, unique = true, length = 10)
    private String code;

    @OneToMany(mappedBy = "country", fetch = FetchType.LAZY)
    @JsonIgnore   // 🚀 Empêche boucle infinie + erreurs serialize + lazy loading
    private List<ProvinceState> provincesStates;

    // ===== Constructeurs =====

    public Country() {}

    public Country(String name, String code) {
        this.name = name;
        this.code = code;
    }

    // ===== Getters / Setters =====

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getCode() { return code; }
    public List<ProvinceState> getProvincesStates() { return provincesStates; }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setCode(String code) { this.code = code; }
    public void setProvincesStates(List<ProvinceState> provincesStates) {
        this.provincesStates = provincesStates;
    }
}
