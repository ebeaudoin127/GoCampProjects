package com.gocamp.reservecamping.campsite.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "campsite_surface_type",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_campsite_surface_type",
                        columnNames = {"campsite_id", "site_surface_type_id"}
                )
        }
)
public class CampsiteSurfaceType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "campsite_id", nullable = false)
    private Campsite campsite;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "site_surface_type_id", nullable = false)
    private SiteSurfaceType siteSurfaceType;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Long getId() { return id; }
    public Campsite getCampsite() { return campsite; }
    public void setCampsite(Campsite campsite) { this.campsite = campsite; }
    public SiteSurfaceType getSiteSurfaceType() { return siteSurfaceType; }
    public void setSiteSurfaceType(SiteSurfaceType siteSurfaceType) { this.siteSurfaceType = siteSurfaceType; }
}