// ============================================================
// Fichier : CampgroundMap.java
// Chemin : campmap/model
// Dernière modification : 2026-04-18
//
// Résumé :
// - Carte principale d’un camping
// - Contient image de fond + dimensions
// ============================================================

package com.gocamp.reservecamping.campmap.model;

import com.gocamp.reservecamping.campground.model.Campground;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "campground_map",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_campground_map", columnNames = {"campground_id"})
        }
)
public class CampgroundMap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campground_id", nullable = false)
    private Campground campground;

    @Column(name = "background_image_path")
    private String backgroundImagePath;

    @Column(name = "image_width")
    private Integer imageWidth;

    @Column(name = "image_height")
    private Integer imageHeight;

    private Boolean isActive = true;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Campground getCampground() { return campground; }
    public void setCampground(Campground campground) { this.campground = campground; }

    public String getBackgroundImagePath() { return backgroundImagePath; }
    public void setBackgroundImagePath(String backgroundImagePath) { this.backgroundImagePath = backgroundImagePath; }

    public Integer getImageWidth() { return imageWidth; }
    public void setImageWidth(Integer imageWidth) { this.imageWidth = imageWidth; }

    public Integer getImageHeight() { return imageHeight; }
    public void setImageHeight(Integer imageHeight) { this.imageHeight = imageHeight; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean active) { isActive = active; }
}
