// ============================================================
// Fichier : PromotionType.java
// Dernière modification : 2026-05-04
//
// Résumé :
// - Types de promotions supportés
// ============================================================

package com.gocamp.reservecamping.campsite.model;

public enum PromotionType {
    FIXED_PRICE,
    PERCENT_DISCOUNT,
    BUY_X_PAY_Y,
    AMOUNT_DISCOUNT,
    X_NIGHTS_FOR_AMOUNT,
    CONSECUTIVE_WEEKENDS,
    PACKAGE,
    OTHER
}