// ============================================================
// Fichier : AuthController.java
// Chemin  : backend/src/main/java/com/gocamp/reservecamping/auth
// Dernière modification : 2026-05-14
// Auteur : ChatGPT pour Eric Beaudoin
//
// Résumé :
// - Gestion authentification JWT
// - Endpoint /auth/me utilisé par le frontend
// - Retourne maintenant les coordonnées complètes du client
//   pour la confirmation de réservation
//
// Historique des modifications :
// 2026-04-16
// - /auth/me retourne maintenant MeResponse
// - rôle converti en STRING
//
// 2026-05-14
// - Ajout phone dans /auth/me
// - Ajout address dans /auth/me
// - Ajout city dans /auth/me
// - Ajout postalCode dans /auth/me
// - Ajout countryName dans /auth/me
// - Ajout provinceStateName dans /auth/me
// ============================================================

package com.gocamp.reservecamping.auth;

import com.gocamp.reservecamping.auth.dto.ChangePasswordRequest;
import com.gocamp.reservecamping.auth.dto.LoginRequest;
import com.gocamp.reservecamping.auth.dto.RegisterRequest;
import com.gocamp.reservecamping.user.User;
import com.gocamp.reservecamping.user.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService service;
    private final UserRepository userRepo;

    public AuthController(AuthService service, UserRepository userRepo) {
        this.service = service;
        this.userRepo = userRepo;
    }

    // ============================================
    // REGISTER
    // ============================================
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        String token = service.register(req);
        return ResponseEntity.ok(Map.of("token", token));
    }

    // ============================================
    // LOGIN
    // ============================================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        String token = service.login(req);
        return ResponseEntity.ok(Map.of("token", token));
    }

    // ============================================
    // CURRENT USER
    // ============================================
    @GetMapping("/me")
    public ResponseEntity<?> me(@AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(401).body(
                    Map.of("error", "Token invalide ou expiré")
            );
        }

        User user = userRepo.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        MeResponse response = new MeResponse(
                user.getId(),
                user.getFirstname(),
                user.getLastname(),
                user.getEmail(),
                user.getRole().getName(),

                user.getPhone(),
                user.getAddress(),
                user.getCity(),
                user.getPostalCode(),

                user.getCountry() != null
                        ? user.getCountry().getName()
                        : null,

                user.getProvinceState() != null
                        ? user.getProvinceState().getName()
                        : null
        );

        return ResponseEntity.ok(response);
    }

    // ============================================
    // CHANGE PASSWORD
    // ============================================
    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody ChangePasswordRequest req) {

        User user = userRepo.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        service.changePassword(user, req);

        return ResponseEntity.ok(
                Map.of("message", "Mot de passe mis à jour")
        );
    }
}
