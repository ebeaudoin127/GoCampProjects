package com.gocamp.reservecamping.user.dto;

import com.gocamp.reservecamping.location.Country;
import com.gocamp.reservecamping.location.ProvinceState;
import com.gocamp.reservecamping.role.Role;
import com.gocamp.reservecamping.user.User;

public record UserDTO(
        Long id,
        String firstname,
        String lastname,
        String email,
        RoleDTO role,
        String phone,
        String address,
        String city,
        String postalCode,
        CountryDTO country,
        ProvinceStateDTO provinceState
) {

    public static UserDTO from(User user) {
        return new UserDTO(
                user.getId(),
                user.getFirstname(),
                user.getLastname(),
                user.getEmail(),
                user.getRole() != null ? RoleDTO.from(user.getRole()) : null,
                user.getPhone(),
                user.getAddress(),
                user.getCity(),
                user.getPostalCode(),
                user.getCountry() != null ? CountryDTO.from(user.getCountry()) : null,
                user.getProvinceState() != null ? ProvinceStateDTO.from(user.getProvinceState()) : null);
    }

    // --- Nested DTOs ---
    public record RoleDTO(Long id, String name) {
        public static RoleDTO from(Role r) {
            return new RoleDTO(r.getId(), r.getName());
        }
    }

    public record CountryDTO(Long id, String name) {
        public static CountryDTO from(Country c) {
            return new CountryDTO(c.getId(), c.getName());
        }
    }

    public record ProvinceStateDTO(Long id, String name) {
        public static ProvinceStateDTO from(ProvinceState ps) {
            return new ProvinceStateDTO(ps.getId(), ps.getName());
        }
    }
}
