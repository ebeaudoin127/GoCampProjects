package com.gocamp.reservecamping.campsite.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "campsite_equipment_allowed",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_campsite_equipment_allowed",
                        columnNames = {"campsite_id", "equipment_allowed_type_id"}
                )
        }
)
public class CampsiteEquipmentAllowed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "campsite_id", nullable = false)
    private Campsite campsite;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "equipment_allowed_type_id", nullable = false)
    private EquipmentAllowedType equipmentAllowedType;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Long getId() { return id; }
    public Campsite getCampsite() { return campsite; }
    public void setCampsite(Campsite campsite) { this.campsite = campsite; }
    public EquipmentAllowedType getEquipmentAllowedType() { return equipmentAllowedType; }
    public void setEquipmentAllowedType(EquipmentAllowedType equipmentAllowedType) { this.equipmentAllowedType = equipmentAllowedType; }
}