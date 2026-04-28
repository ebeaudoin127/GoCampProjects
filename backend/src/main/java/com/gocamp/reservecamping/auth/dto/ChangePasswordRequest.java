package com.gocamp.reservecamping.auth.dto;

public record ChangePasswordRequest(
        String oldPassword,
        String newPassword
) {}
