// ============================================================
// Fichier : PricingTargetType.java
// Dernière modification : 2026-05-06
//
// Résumé :
// - Types de cibles pour les règles tarifaires et promotions
// - Supporte site, regroupement, tout le camping, multi-sites
//   et sites disponibles / non réservés
// ============================================================

package com.gocamp.reservecamping.campsite.model;

public enum PricingTargetType {
    SITE,
    GROUP,
    ALL_CAMPGROUND,
    MULTI_CAMPSITE,
    AVAILABLE_CAMPSITES
}
