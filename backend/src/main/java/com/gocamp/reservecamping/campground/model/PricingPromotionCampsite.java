// ============================================================
// Fichier : PricingPromotionCampsite.java
// Dernière modification : 2026-05-04
//
// Résumé :
// - Association entre une promotion tarifaire et plusieurs sites
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
        }
)
public class PricingPromotionCampsite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pricing_promotion_id", nullable = false)
    private PricingPromotion pricingPromotion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
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
