
package com.gocamp.reservecamping.campsite.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class NightPriceBreakdownDto {

    private LocalDate date;

    private String pricingRuleName;

    private BigDecimal basePrice;

    private BigDecimal discountAmount;

    private BigDecimal finalPrice;

    private String appliedPromotion;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getPricingRuleName() {
        return pricingRuleName;
    }

    public void setPricingRuleName(String pricingRuleName) {
        this.pricingRuleName = pricingRuleName;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(BigDecimal finalPrice) {
        this.finalPrice = finalPrice;
    }

    public String getAppliedPromotion() {
        return appliedPromotion;
    }

    public void setAppliedPromotion(String appliedPromotion) {
        this.appliedPromotion = appliedPromotion;
    }
}