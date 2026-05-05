// ============================================================
// Fichier : CampgroundReservationPeriod.java
// Dernière modification : 2026-05-04
// Auteur : ChatGPT
//
// Résumé :
// - Entité représentant une période de réservation d'un camping
// - Un camping peut avoir plusieurs périodes sur plusieurs années
// - Sert à contrôler les dates où la réservation est permise
// ============================================================

package com.gocamp.reservecamping.campground.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "campground_reservation_period",
        indexes = {
                @Index(name = "idx_campground_reservation_period_campground", columnList = "campground_id"),
                @Index(name = "idx_campground_reservation_period_dates", columnList = "start_date,end_date"),
                @Index(name = "idx_campground_reservation_period_active", columnList = "active")
        }
)
public class CampgroundReservationPeriod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "campground_id", nullable = false)
    private Campground campground;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;

        if (this.active == null) {
            this.active = true;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();

        if (this.active == null) {
            this.active = true;
        }
    }

    public CampgroundReservationPeriod() {
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

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}