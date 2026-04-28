// ============================================================
// Fichier : backend/src/main/java/com/gocamp/reservecamping/campsite/model/CampsitePricingRule.java
// Dernière modification : 2026-04-24
//
// Résumé :
// - Règle tarifaire par site ou regroupement
// - Supporte FIXED / DYNAMIC
// - Supporte dynamicBasePrice
// - Ajout minimumNights pour séjour minimum obligatoire
// ============================================================

package com.gocamp.reservecamping.campsite.model;

import com.gocamp.reservecamping.campground.model.Campground;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "campsite_pricing_rule")
public class CampsitePricingRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "campground_id", nullable = false)
    private Campground campground;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_type", nullable = false, length = 20)
    private PricingTargetType targetType;

    @Enumerated(EnumType.STRING)
    @Column(name = "pricing_type", nullable = false, length = 20)
    private PricingType pricingType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campsite_id")
    private Campsite campsite;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pricing_option_id")
    private CampgroundSitePricingOption pricingOption;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "fixed_price", precision = 10, scale = 2)
    private BigDecimal fixedPrice;

    @Column(name = "dynamic_base_price", precision = 10, scale = 2)
    private BigDecimal dynamicBasePrice;

    @Column(name = "dynamic_min_price", precision = 10, scale = 2)
    private BigDecimal dynamicMinPrice;

    @Column(name = "dynamic_max_price", precision = 10, scale = 2)
    private BigDecimal dynamicMaxPrice;

    @Column(name = "minimum_nights")
    private Integer minimumNights;

    @Column(name = "label", length = 100)
    private String label;

    @Column(name = "notes", length = 1000)
    private String notes;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }

    public Campground getCampground() { return campground; }
    public void setCampground(Campground campground) { this.campground = campground; }

    public PricingTargetType getTargetType() { return targetType; }
    public void setTargetType(PricingTargetType targetType) { this.targetType = targetType; }

    public PricingType getPricingType() { return pricingType; }
    public void setPricingType(PricingType pricingType) { this.pricingType = pricingType; }

    public Campsite getCampsite() { return campsite; }
    public void setCampsite(Campsite campsite) { this.campsite = campsite; }

    public CampgroundSitePricingOption getPricingOption() { return pricingOption; }
    public void setPricingOption(CampgroundSitePricingOption pricingOption) { this.pricingOption = pricingOption; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public BigDecimal getFixedPrice() { return fixedPrice; }
    public void setFixedPrice(BigDecimal fixedPrice) { this.fixedPrice = fixedPrice; }

    public BigDecimal getDynamicBasePrice() { return dynamicBasePrice; }
    public void setDynamicBasePrice(BigDecimal dynamicBasePrice) { this.dynamicBasePrice = dynamicBasePrice; }

    public BigDecimal getDynamicMinPrice() { return dynamicMinPrice; }
    public void setDynamicMinPrice(BigDecimal dynamicMinPrice) { this.dynamicMinPrice = dynamicMinPrice; }

    public BigDecimal getDynamicMaxPrice() { return dynamicMaxPrice; }
    public void setDynamicMaxPrice(BigDecimal dynamicMaxPrice) { this.dynamicMaxPrice = dynamicMaxPrice; }

    public Integer getMinimumNights() { return minimumNights; }
    public void setMinimumNights(Integer minimumNights) { this.minimumNights = minimumNights; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}