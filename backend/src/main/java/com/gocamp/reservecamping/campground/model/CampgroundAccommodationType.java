// ============================================================
// Fichier : CampgroundAccommodationType.java
// Dernière modification : 2026-04-16
// Auteur : ChatGPT
//
// Résumé :
// - Table d’association entre campground et accommodation_type
// ============================================================

package com.gocamp.reservecamping.campground.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "campground_accommodation_type",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_campground_accommodation_type",
                        columnNames = {"campground_id", "accommodation_type_id"}
                )
        },
        indexes = {
                @Index(name = "idx_campground_accommodation_type_campground", columnList = "campground_id"),
                @Index(name = "idx_campground_accommodation_type_type", columnList = "accommodation_type_id")
        }
)
public class CampgroundAccommodationType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "campground_id", nullable = false)
    private Campground campground;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "accommodation_type_id", nullable = false)
    private AccommodationType accommodationType;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "notes", length = 255)
    private String notes;

    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        if (this.isAvailable == null) this.isAvailable = true;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public CampgroundAccommodationType() {
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

    public AccommodationType getAccommodationType() {
        return accommodationType;
    }

    public void setAccommodationType(AccommodationType accommodationType) {
        this.accommodationType = accommodationType;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean available) {
        isAvailable = available;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}