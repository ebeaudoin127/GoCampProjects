package com.gocamp.reservecamping.auth.dto;

public record LoginRequest(
        String email,
        String password
) {}
