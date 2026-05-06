// ============================================================
// Fichier : backend/src/main/java/com/gocamp/reservecamping/campsite/model/PricingPromotion.java
// Dernière modification : 2026-05-05
//
// Résumé :
// - Promotion ponctuelle pouvant bypasser ou ajuster
//   la tarification normale
// - Supporte SITE, GROUP, MULTI_CAMPSITE et ALL_CAMPGROUND
// - Supporte promotions marketing avancées simples
// ============================================================

package com.gocamp.reservecamping.campsite.model;

import com.gocamp.reservecamping.campground.model.Campground;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "pricing_promotion")
public class PricingPromotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "campground_id", nullable = false)
    private Campground campground;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_type", nullable = false, length = 20)
    private PricingTargetType targetType;

    @Enumerated(EnumType.STRING)
    @Column(name = "application_mode", nullable = false, length = 20)
    private PromotionApplicationMode applicationMode;

    @Enumerated(EnumType.STRING)
    @Column(name = "promotion_type", nullable = false, length = 30)
    private PromotionType promotionType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campsite_id")
    private Campsite campsite;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pricing_option_id")
    private CampgroundSitePricingOption pricingOption;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "name", nullable = false, length = 120)
    private String name;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "fixed_price", precision = 10, scale = 2)
    private BigDecimal fixedPrice;

    @Column(name = "discount_percent", precision = 5, scale = 2)
    private BigDecimal discountPercent;

    @Column(name = "discount_amount", precision = 10, scale = 2)
    private BigDecimal discountAmount;

    @Column(name = "buy_nights")
    private Integer buyNights;

    @Column(name = "pay_nights")
    private Integer payNights;

    @Column(name = "package_nights")
    private Integer packageNights;

    @Column(name = "package_price", precision = 10, scale = 2)
    private BigDecimal packagePrice;

    @Column(name = "required_consecutive_weekends")
    private Integer requiredConsecutiveWeekends;

    @Column(name = "min_nights")
    private Integer minNights;

    @Column(name = "max_nights")
    private Integer maxNights;

    @Column(name = "priority", nullable = false)
    private Integer priority = 100;

    @Column(name = "combinable", nullable = false)
    private boolean combinable = false;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @Column(name = "promo_code", length = 100)
    private String promoCode;

    @Column(name = "requires_promo_code", nullable = false)
    private boolean requiresPromoCode = false;

    @Column(name = "booking_before_date")
    private LocalDate bookingBeforeDate;

    @Column(name = "arrival_within_days")
    private Integer arrivalWithinDays;

    @Enumerated(EnumType.STRING)
    @Column(name = "required_arrival_day", length = 20)
    private PricingDayOfWeek requiredArrivalDay;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
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

    public PricingTargetType getTargetType() {
        return targetType;
    }

    public void setTargetType(PricingTargetType targetType) {
        this.targetType = targetType;
    }

    public PromotionApplicationMode getApplicationMode() {
        return applicationMode;
    }

    public void setApplicationMode(PromotionApplicationMode applicationMode) {
        this.applicationMode = applicationMode;
    }

    public PromotionType getPromotionType() {
        return promotionType;
    }

    public void setPromotionType(PromotionType promotionType) {
        this.promotionType = promotionType;
    }

    public Campsite getCampsite() {
        return campsite;
    }

    public void setCampsite(Campsite campsite) {
        this.campsite = campsite;
    }

    public CampgroundSitePricingOption getPricingOption() {
        return pricingOption;
    }

    public void setPricingOption(CampgroundSitePricingOption pricingOption) {
        this.pricingOption = pricingOption;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getFixedPrice() {
        return fixedPrice;
    }

    public void setFixedPrice(BigDecimal fixedPrice) {
        this.fixedPrice = fixedPrice;
    }

    public BigDecimal getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(BigDecimal discountPercent) {
        this.discountPercent = discountPercent;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Integer getBuyNights() {
        return buyNights;
    }

    public void setBuyNights(Integer buyNights) {
        this.buyNights = buyNights;
    }

    public Integer getPayNights() {
        return payNights;
    }

    public void setPayNights(Integer payNights) {
        this.payNights = payNights;
    }

    public Integer getPackageNights() {
        return packageNights;
    }

    public void setPackageNights(Integer packageNights) {
        this.packageNights = packageNights;
    }

    public BigDecimal getPackagePrice() {
        return packagePrice;
    }

    public void setPackagePrice(BigDecimal packagePrice) {
        this.packagePrice = packagePrice;
    }

    public Integer getRequiredConsecutiveWeekends() {
        return requiredConsecutiveWeekends;
    }

    public void setRequiredConsecutiveWeekends(Integer requiredConsecutiveWeekends) {
        this.requiredConsecutiveWeekends = requiredConsecutiveWeekends;
    }

    public Integer getMinNights() {
        return minNights;
    }

    public void setMinNights(Integer minNights) {
        this.minNights = minNights;
    }

    public Integer getMaxNights() {
        return maxNights;
    }

    public void setMaxNights(Integer maxNights) {
        this.maxNights = maxNights;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public boolean isCombinable() {
        return combinable;
    }

    public void setCombinable(boolean combinable) {
        this.combinable = combinable;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public boolean isRequiresPromoCode() {
        return requiresPromoCode;
    }

    public void setRequiresPromoCode(boolean requiresPromoCode) {
        this.requiresPromoCode = requiresPromoCode;
    }

    public LocalDate getBookingBeforeDate() {
        return bookingBeforeDate;
    }

    public void setBookingBeforeDate(LocalDate bookingBeforeDate) {
        this.bookingBeforeDate = bookingBeforeDate;
    }

    public Integer getArrivalWithinDays() {
        return arrivalWithinDays;
    }

    public void setArrivalWithinDays(Integer arrivalWithinDays) {
        this.arrivalWithinDays = arrivalWithinDays;
    }

    public PricingDayOfWeek getRequiredArrivalDay() {
        return requiredArrivalDay;
    }

    public void setRequiredArrivalDay(PricingDayOfWeek requiredArrivalDay) {
        this.requiredArrivalDay = requiredArrivalDay;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}