// ============================================================
// Fichier : AuthService.java
// Dernière modification : 2025-11-17
// Auteur : ChatGPT (corrections pour Eric Beaudoin)
// Résumé : l'inscription exige countryId & provinceStateId,
//          vérifie que la province appartient au pays,
//          puis enregistre les deux dans User.
// ============================================================

package com.gocamp.reservecamping.auth;

import com.gocamp.reservecamping.auth.dto.*;
import com.gocamp.reservecamping.jwt.JwtService;
import com.gocamp.reservecamping.role.Role;
import com.gocamp.reservecamping.role.RoleRepository;

// 🔥 AJOUTÉ
import com.gocamp.reservecamping.location.Country;
import com.gocamp.reservecamping.location.CountryRepository;
import com.gocamp.reservecamping.location.ProvinceState;
import com.gocamp.reservecamping.location.ProvinceStateRepository;

import com.gocamp.reservecamping.user.User;
import com.gocamp.reservecamping.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;

    // 🔥 AJOUTÉ
    private final CountryRepository countryRepo;
    private final ProvinceStateRepository provinceStateRepo;

    private final PasswordEncoder encoder;
    private final JwtService jwt;

    public AuthService(UserRepository userRepo,
                       RoleRepository roleRepo,
                       CountryRepository countryRepo,          // 🔥 AJOUTÉ
                       ProvinceStateRepository provinceStateRepo, // 🔥 AJOUTÉ
                       PasswordEncoder encoder,
                       JwtService jwt) {

        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.countryRepo = countryRepo;               // 🔥 AJOUTÉ
        this.provinceStateRepo = provinceStateRepo;   // 🔥 AJOUTÉ
        this.encoder = encoder;
        this.jwt = jwt;
    }

    // -----------------------------
    // REGISTER
    // -----------------------------
    public String register(RegisterRequest req) {

        if (userRepo.findByEmail(req.email()).isPresent()) {
            throw new RuntimeException("Un compte existe déjà avec cet email.");
        }

        // 🔥 AJOUTÉ — valider pays
        Country country = countryRepo.findById(req.countryId())
                .orElseThrow(() -> new RuntimeException("Pays non valide"));

        // 🔥 AJOUTÉ — valider province/état
        ProvinceState provinceState = provinceStateRepo.findById(req.provinceStateId())
                .orElseThrow(() -> new RuntimeException("Province/État non valide"));

        // 🔥 AJOUTÉ — valider que la province appartient au pays
        if (!provinceState.getCountry().getId().equals(country.getId())) {
            throw new RuntimeException("Cette province/état n'appartient pas au pays sélectionné.");
        }

        Role role = roleRepo.findByName("UTILISATEUR")
                .orElseThrow(() -> new RuntimeException("Role UTILISATEUR non trouvé"));

        User u = new User();
        u.setFirstname(req.firstname());
        u.setLastname(req.lastname());
        u.setEmail(req.email());
        u.setPassword(encoder.encode(req.password()));
        u.setRole(role);

        // 🔥 AJOUTÉ
        u.setCountry(country);
        u.setProvinceState(provinceState);

        userRepo.save(u);

        return jwt.generateToken(u.getEmail());
    }

    // -----------------------------
    // LOGIN
    // -----------------------------
    public String login(LoginRequest req) {
        User u = userRepo.findByEmail(req.email())
                .orElseThrow(() -> new RuntimeException("Identifiants invalides"));

        if (!encoder.matches(req.password(), u.getPassword())) {
            throw new RuntimeException("Identifiants invalides");
        }

        return jwt.generateToken(u.getEmail());
    }

    // -----------------------------
    // CHANGE PASSWORD
    // -----------------------------
    public void changePassword(User user, ChangePasswordRequest req) {

        if (!encoder.matches(req.oldPassword(), user.getPassword())) {
            throw new RuntimeException("Ancien mot de passe incorrect");
        }

        user.setPassword(encoder.encode(req.newPassword()));
        userRepo.save(user);
    }
}
