// ============================================================
// Fichier : backend/src/main/java/com/gocamp/reservecamping/campsite/model/CampsitePricingRuleDay.java
// Dernière modification : 2026-04-20
//
// Résumé :
// - Jours de semaine applicables à une règle tarifaire
// ============================================================

package com.gocamp.reservecamping.campsite.model;

import jakarta.persistence.*;

@Entity
@Table(name = "campsite_pricing_rule_day")
public class CampsitePricingRuleDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "pricing_rule_id", nullable = false)
    private CampsitePricingRule pricingRule;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false, length = 20)
    private PricingDayOfWeek dayOfWeek;

    public Long getId() {
        return id;
    }

    public CampsitePricingRule getPricingRule() {
        return pricingRule;
    }

    public void setPricingRule(CampsitePricingRule pricingRule) {
        this.pricingRule = pricingRule;
    }

    public PricingDayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(PricingDayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
}