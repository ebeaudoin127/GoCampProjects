// ============================================================
// Fichier : PricingPromotionCampsite.java
// Dernière modification : 2026-05-04
//
// Résumé :
// - Association entre une promotion tarifaire dynamique et plusieurs sites
// - Permet les promotions ciblant une sélection précise de sites
// ============================================================

package com.gocamp.reservecamping.campsite.model;

import jakarta.persistence.*;

@Entity
@Table(
        name = "pricing_promotion_campsite",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_pricing_promotion_campsite",
                        columnNames = {"pricing_promotion_id", "campsite_id"}
                )
        },
        indexes = {
                @Index(name = "idx_ppc_promotion", columnList = "pricing_promotion_id"),
                @Index(name = "idx_ppc_campsite", columnList = "campsite_id")
        }
)
public class PricingPromotionCampsite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "pricing_promotion_id", nullable = false)
    private PricingPromotion pricingPromotion;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "campsite_id", nullable = false)
    private Campsite campsite;

    public Long getId() {
        return id;
    }

    public PricingPromotion getPricingPromotion() {
        return pricingPromotion;
    }

    public void setPricingPromotion(PricingPromotion pricingPromotion) {
        this.pricingPromotion = pricingPromotion;
    }

    public Campsite getCampsite() {
        return campsite;
    }

    public void setCampsite(Campsite campsite) {
        this.campsite = campsite;
    }
}
