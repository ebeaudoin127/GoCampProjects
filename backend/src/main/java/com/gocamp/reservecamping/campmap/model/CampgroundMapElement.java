// ============================================================
// Fichier : CampgroundMapElement.java
// Chemin : campmap/model
// Dernière modification : 2026-04-18
//
// Résumé :
// - Élément non-site sur la carte
// - Exemple : piscine, accueil, etc.
// ============================================================

package com.gocamp.reservecamping.campmap.model;

import jakarta.persistence.*;

@Entity
@Table(name = "campground_map_element")
public class CampgroundMapElement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long campgroundId;

    @ManyToOne
    @JoinColumn(name = "element_type_id")
    private MapElementType type;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String polygonJson;

    private Integer labelX;
    private Integer labelY;

    private Boolean isActive = true;

    public Long getId() { return id; }

    public Long getCampgroundId() { return campgroundId; }
    public void setCampgroundId(Long campgroundId) { this.campgroundId = campgroundId; }

    public MapElementType getType() { return type; }
    public void setType(MapElementType type) { this.type = type; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPolygonJson() { return polygonJson; }
    public void setPolygonJson(String polygonJson) { this.polygonJson = polygonJson; }

    public Integer getLabelX() { return labelX; }
    public void setLabelX(Integer labelX) { this.labelX = labelX; }

    public Integer getLabelY() { return labelY; }
    public void setLabelY(Integer labelY) { this.labelY = labelY; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean active) { isActive = active; }
}