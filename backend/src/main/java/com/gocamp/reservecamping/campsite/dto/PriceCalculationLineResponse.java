// ============================================================
// Fichier : PriceCalculationLineResponse.java
// Dernière modification : 2026-05-05
// Auteur : ChatGPT
//
// Résumé :
// - Ligne de détail du calculateur
// - Représente une nuit facturée
// ============================================================

package com.gocamp.reservecamping.campsite.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PriceCalculationLineResponse(
        LocalDate date,
        String label,
        BigDecimal amount
) {}