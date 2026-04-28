// ============================================================
// Fichier : backend/src/main/java/com/gocamp/reservecamping/campsite/model/PricingPromotionDay.java
// Dernière modification : 2026-04-20
//
// Résumé :
// - Jours de semaine applicables à une promotion ponctuelle
// ============================================================

package com.gocamp.reservecamping.campsite.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pricing_promotion_day")
public class PricingPromotionDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "pricing_promotion_id", nullable = false)
    private PricingPromotion pricingPromotion;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false, length = 20)
    private PricingDayOfWeek dayOfWeek;

    public Long getId() {
        return id;
    }

    public PricingPromotion getPricingPromotion() {
        return pricingPromotion;
    }

    public void setPricingPromotion(PricingPromotion pricingPromotion) {
        this.pricingPromotion = pricingPromotion;
    }

    public PricingDayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(PricingDayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
}