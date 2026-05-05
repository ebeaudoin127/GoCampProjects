// ============================================================
// Fichier : PriceCalculatorController.java
// ============================================================

package com.gocamp.reservecamping.campsite.controller;

import com.gocamp.reservecamping.campsite.dto.*;
import com.gocamp.reservecamping.campsite.service.PriceCalculatorService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pricing-calculator")
@CrossOrigin("*")
public class PriceCalculatorController {

    private final PriceCalculatorService service;

    public PriceCalculatorController(PriceCalculatorService service) {
        this.service = service;
    }

    @PostMapping
    public PriceCalculationResponse calculate(@RequestBody PriceCalculationRequest req) {
        return service.calculate(req);
    }
}