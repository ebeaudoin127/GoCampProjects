// ============================================================
// Fichier : backend/src/main/java/com/gocamp/reservecamping/pricing/controller/PricingEngineController.java
// Dernière modification : 2026-04-20
//
// Résumé :
// - API REST pour tester le moteur de tarification
// - Permet de calculer un prix pour un site, une date et un
//   nombre de nuits
// ============================================================

package com.gocamp.reservecamping.pricing.controller;

import com.gocamp.reservecamping.pricing.dto.PriceCalculationRequest;
import com.gocamp.reservecamping.pricing.dto.PriceCalculationResponse;
import com.gocamp.reservecamping.pricing.service.PricingEngineService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pricing-engine")
@CrossOrigin("*")
public class PricingEngineController {

    private final PricingEngineService pricingEngineService;

    public PricingEngineController(PricingEngineService pricingEngineService) {
        this.pricingEngineService = pricingEngineService;
    }

    @PostMapping("/calculate")
    public PriceCalculationResponse calculate(@RequestBody PriceCalculationRequest request) {
        return pricingEngineService.calculate(request);
    }
}