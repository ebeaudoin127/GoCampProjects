package com.gocamp.reservecamping.location;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/province-states")
@CrossOrigin(origins = "*")
public class ProvinceStateController {

    @Autowired
    private ProvinceStateRepository provinceStateRepository;

    @GetMapping("/by-country/{countryId}")
    public List<ProvinceState> getByCountry(@PathVariable Long countryId) {
        return provinceStateRepository.findByCountryId(countryId);
    }
}
