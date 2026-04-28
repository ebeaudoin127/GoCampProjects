// ============================================================
// Fichier : MapElementType.java
// Chemin : campmap/model
// Dernière modification : 2026-04-18
//
// Résumé :
// - Type d’élément de carte (piscine, bloc sanitaire, etc.)
// ============================================================

package com.gocamp.reservecamping.campmap.model;

import jakarta.persistence.*;

@Entity
@Table(name = "map_element_type")
public class MapElementType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private String nameFr;

    private Boolean isActive = true;

    public Long getId() { return id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getNameFr() { return nameFr; }
    public void setNameFr(String nameFr) { this.nameFr = nameFr; }
}
