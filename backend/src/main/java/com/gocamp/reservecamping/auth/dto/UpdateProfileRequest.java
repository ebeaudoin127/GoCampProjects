package com.gocamp.reservecamping.auth.dto;

public record UpdateProfileRequest(
        String firstname,
        String lastname,
        String phone,
        String address,
        String city,
        String postalCode,
        Long countryId,
        Long provinceStateId,
        String equipmentType,
        Integer equipmentLength,
        Boolean hasSlideOut
) {}
