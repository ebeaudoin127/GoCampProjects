// ============================================================
// Fichier : AuthController.java
// Modifié : 2026-04-16
//
// Résumé :
// - /auth/me retourne maintenant MeResponse
// - rôle converti en STRING
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
    // 🔥 VERSION PROPRE
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

        // 🔥 Conversion en DTO
        MeResponse response = new MeResponse(
                user.getId(),
                user.getFirstname(),
                user.getLastname(),
                user.getEmail(),
                user.getRole().getName() // IMPORTANT
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
        return ResponseEntity.ok(Map.of("message", "Mot de passe mis à jour"));
    }
}