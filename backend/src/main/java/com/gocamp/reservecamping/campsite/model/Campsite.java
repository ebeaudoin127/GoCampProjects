// ============================================================
// Fichier : Campsite.java
// Chemin  : backend/src/main/java/com/gocamp/reservecamping/campsite/model
// Dernière modification : 2026-05-09
// Auteur : ChatGPT pour Eric Beaudoin
//
// Résumé :
// - Entité JPA des sites
// - Ajout des services disponibles en booléens
// - Ajout des ampérages disponibles en booléens
// - Conservation temporaire de siteServiceType et siteAmperage
//
// Historique des modifications :
// 2026-04-18
// - Ajout du champ mapPolygonJson
// - Ajout de labelX / labelY pour affichage sur la carte
//
// 2026-05-09
// - Ajout hasWater / hasElectricity / hasSewer
// - Ajout has15_20Amp / has30Amp / has50Amp
// ============================================================

package com.gocamp.reservecamping.campsite.model;

import com.gocamp.reservecamping.campground.model.Campground;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "campsite",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_campsite_campground_site_code",
                        columnNames = {"campground_id", "site_code"}
                )
        }
)
public class Campsite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campground_id", nullable = false)
    private Campground campground;

    @Column(name = "site_code", nullable = false, length = 50)
    private String siteCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_type_id")
    private SiteType siteType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_service_type_id")
    private SiteServiceType siteServiceType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_amperage_id")
    private SiteAmperage siteAmperage;

    @Column(name = "has_water", nullable = false)
    private boolean hasWater = false;

    @Column(name = "has_electricity", nullable = false)
    private boolean hasElectricity = false;

    @Column(name = "has_sewer", nullable = false)
    private boolean hasSewer = false;

    @Column(name = "has_15_20_amp", nullable = false)
    private boolean has15_20Amp = false;

    @Column(name = "has_30_amp", nullable = false)
    private boolean has30Amp = false;

    @Column(name = "has_50_amp", nullable = false)
    private boolean has50Amp = false;

    @Column(name = "width_feet")
    private BigDecimal widthFeet;

    @Column(name = "length_feet")
    private BigDecimal lengthFeet;

    @Column(name = "max_equipment_length_feet")
    private BigDecimal maxEquipmentLengthFeet;

    @Column(name = "is_pull_through")
    private boolean isPullThrough;

    @Column(name = "is_active")
    private boolean isActive = true;

    @Column(length = 1000)
    private String notes;

    @Column(name = "map_polygon_json", columnDefinition = "TEXT")
    private String mapPolygonJson;

    @Column(name = "label_x")
    private Integer labelX;

    @Column(name = "label_y")
    private Integer labelY;

    @OneToMany(mappedBy = "campsite", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CampsiteEquipmentAllowed> equipmentAllowed = new ArrayList<>();

    @OneToMany(mappedBy = "campsite", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CampsiteSurfaceType> surfaceTypes = new ArrayList<>();

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Campground getCampground() {
        return campground;
    }

    public void setCampground(Campground campground) {
        this.campground = campground;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public SiteType getSiteType() {
        return siteType;
    }

    public void setSiteType(SiteType siteType) {
        this.siteType = siteType;
    }

    public SiteServiceType getSiteServiceType() {
        return siteServiceType;
    }

    public void setSiteServiceType(SiteServiceType siteServiceType) {
        this.siteServiceType = siteServiceType;
    }

    public SiteAmperage getSiteAmperage() {
        return siteAmperage;
    }

    public void setSiteAmperage(SiteAmperage siteAmperage) {
        this.siteAmperage = siteAmperage;
    }

    public boolean isHasWater() {
        return hasWater;
    }

    public void setHasWater(boolean hasWater) {
        this.hasWater = hasWater;
    }

    public boolean isHasElectricity() {
        return hasElectricity;
    }

    public void setHasElectricity(boolean hasElectricity) {
        this.hasElectricity = hasElectricity;
    }

    public boolean isHasSewer() {
        return hasSewer;
    }

    public void setHasSewer(boolean hasSewer) {
        this.hasSewer = hasSewer;
    }

    public boolean isHas15_20Amp() {
        return has15_20Amp;
    }

    public void setHas15_20Amp(boolean has15_20Amp) {
        this.has15_20Amp = has15_20Amp;
    }

    public boolean isHas30Amp() {
        return has30Amp;
    }

    public void setHas30Amp(boolean has30Amp) {
        this.has30Amp = has30Amp;
    }

    public boolean isHas50Amp() {
        return has50Amp;
    }

    public void setHas50Amp(boolean has50Amp) {
        this.has50Amp = has50Amp;
    }

    public BigDecimal getWidthFeet() {
        return widthFeet;
    }

    public void setWidthFeet(BigDecimal widthFeet) {
        this.widthFeet = widthFeet;
    }

    public BigDecimal getLengthFeet() {
        return lengthFeet;
    }

    public void setLengthFeet(BigDecimal lengthFeet) {
        this.lengthFeet = lengthFeet;
    }

    public BigDecimal getMaxEquipmentLengthFeet() {
        return maxEquipmentLengthFeet;
    }

    public void setMaxEquipmentLengthFeet(BigDecimal maxEquipmentLengthFeet) {
        this.maxEquipmentLengthFeet = maxEquipmentLengthFeet;
    }

    public boolean isPullThrough() {
        return isPullThrough;
    }

    public void setPullThrough(boolean pullThrough) {
        isPullThrough = pullThrough;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getMapPolygonJson() {
        return mapPolygonJson;
    }

    public void setMapPolygonJson(String mapPolygonJson) {
        this.mapPolygonJson = mapPolygonJson;
    }

    public Integer getLabelX() {
        return labelX;
    }

    public void setLabelX(Integer labelX) {
        this.labelX = labelX;
    }

    public Integer getLabelY() {
        return labelY;
    }

    public void setLabelY(Integer labelY) {
        this.labelY = labelY;
    }

    public List<CampsiteEquipmentAllowed> getEquipmentAllowed() {
        return equipmentAllowed;
    }

    public void setEquipmentAllowed(List<CampsiteEquipmentAllowed> equipmentAllowed) {
        this.equipmentAllowed = equipmentAllowed;
    }

    public List<CampsiteSurfaceType> getSurfaceTypes() {
        return surfaceTypes;
    }

    public void setSurfaceTypes(List<CampsiteSurfaceType> surfaceTypes) {
        this.surfaceTypes = surfaceTypes;
    }
}
