// ============================================================
// Fichier : backend/src/main/java/com/gocamp/reservecamping/campsite/model/CampsitePricingAssignment.java
// Dernière modification : 2026-04-20
//
// Résumé :
// - Associe un site à une valeur de tarification
// - Un site possède au maximum une valeur de tarification
// ============================================================

package com.gocamp.reservecamping.campsite.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "campsite_pricing_assignment")
public class CampsitePricingAssignment {

    @Id
    @Column(name = "campsite_id")
    private Long campsiteId;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "campsite_id", nullable = false)
    private Campsite campsite;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "pricing_option_id", nullable = false)
    private CampgroundSitePricingOption pricingOption;

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

    public Long getCampsiteId() {
        return campsiteId;
    }

    public Campsite getCampsite() {
        return campsite;
    }

    public void setCampsite(Campsite campsite) {
        this.campsite = campsite;
    }

    public CampgroundSitePricingOption getPricingOption() {
        return pricingOption;
    }

    public void setPricingOption(CampgroundSitePricingOption pricingOption) {
        this.pricingOption = pricingOption;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
