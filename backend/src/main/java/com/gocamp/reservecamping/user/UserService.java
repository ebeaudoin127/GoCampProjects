package com.gocamp.reservecamping.user;

import com.gocamp.reservecamping.location.Country;
import com.gocamp.reservecamping.location.CountryRepository;
import com.gocamp.reservecamping.location.ProvinceState;
import com.gocamp.reservecamping.location.ProvinceStateRepository;
import com.gocamp.reservecamping.auth.dto.UpdateProfileRequest;
import com.gocamp.reservecamping.user.dto.UserProfileRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private ProvinceStateRepository provinceStateRepository;

    // ============================================================================
    // 1️⃣ UPDATE PROFILE POUR UTILISATEUR CONNECTÉ (AuthController → /api/auth/update-profile)
    // ============================================================================
    public User updateProfile(User user, UpdateProfileRequest dto) {

        user.setFirstname(dto.firstname());
        user.setLastname(dto.lastname());
        user.setPhone(dto.phone());
        user.setAddress(dto.address());
        user.setCity(dto.city());
        user.setPostalCode(dto.postalCode());

        if (dto.countryId() != null) {
            Country country = countryRepository.findById(dto.countryId())
                    .orElseThrow(() -> new RuntimeException("Pays invalide"));
            user.setCountry(country);
        }

        if (dto.provinceStateId() != null) {
            ProvinceState province = provinceStateRepository.findById(dto.provinceStateId())
                    .orElseThrow(() -> new RuntimeException("Province invalide"));

            if (user.getCountry() != null &&
                !province.getCountry().getId().equals(user.getCountry().getId())) {
                throw new RuntimeException("Cette province n'appartient pas au pays sélectionné.");
            }

            user.setProvinceState(province);
        }

        return userRepository.save(user);
    }

    // ============================================================================
    // 2️⃣ UPDATE PROFILE PAR ID (UserController → /api/users/{id}/profile)
    // ============================================================================
    public User updateProfile(Long id, UserProfileRequest dto) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        user.setFirstname(dto.getFirstname());
        user.setLastname(dto.getLastname());
        user.setPhone(dto.getPhone());
        user.setAddress(dto.getAddress());
        user.setCity(dto.getCity());
        user.setPostalCode(dto.getPostalCode());

        if (dto.getCountryId() != null) {
            Country country = countryRepository.findById(dto.getCountryId())
                    .orElseThrow(() -> new RuntimeException("Pays invalide"));
            user.setCountry(country);
        }

        if (dto.getProvinceStateId() != null) {
            ProvinceState province = provinceStateRepository.findById(dto.getProvinceStateId())
                    .orElseThrow(() -> new RuntimeException("Province invalide"));

            if (user.getCountry() != null &&
                !province.getCountry().getId().equals(user.getCountry().getId())) {
                throw new RuntimeException("Province n'appartient pas au pays.");
            }

            user.setProvinceState(province);
        }

        return userRepository.save(user);
    }
}
