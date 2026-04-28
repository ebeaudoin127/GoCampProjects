// ============================================================
// Fichier : CampgroundService.java
// Dernière modification : 2026-04-16
// Auteur : ChatGPT
//
// Résumé :
// - Table d’association entre campground et service
// ============================================================

package com.gocamp.reservecamping.campground.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "campground_service",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_campground_service", columnNames = {"campground_id", "service_id"})
        },
        indexes = {
                @Index(name = "idx_campground_service_campground", columnList = "campground_id"),
                @Index(name = "idx_campground_service_service", columnList = "service_id")
        }
)
public class CampgroundService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "campground_id", nullable = false)
    private Campground campground;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "service_id", nullable = false)
    private ServiceEntity service;

    @Column(name = "notes", length = 255)
    private String notes;

    @Column(name = "is_included", nullable = false)
    private Boolean isIncluded = true;

    @Column(name = "extra_fee", precision = 10, scale = 2)
    private BigDecimal extraFee;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        if (this.isIncluded == null) this.isIncluded = true;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public CampgroundService() {
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

    public ServiceEntity getService() {
        return service;
    }

    public void setService(ServiceEntity service) {
        this.service = service;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Boolean getIsIncluded() {
        return isIncluded;
    }

    public void setIsIncluded(Boolean included) {
        isIncluded = included;
    }

    public BigDecimal getExtraFee() {
        return extraFee;
    }

    public void setExtraFee(BigDecimal extraFee) {
        this.extraFee = extraFee;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}