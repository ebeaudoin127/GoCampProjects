// ============================================================
// Fichier : SiteAdminUserController.java
// Dernière modification : 2026-04-16
// Auteur : ChatGPT
//
// Résumé :
// - Contrôleur admin pour la gestion des utilisateurs
// - Accessible uniquement au SUPER_ADMIN
// ============================================================

package com.gocamp.reservecamping.admin;

import com.gocamp.reservecamping.admin.dto.UpdateUserRoleRequest;
import com.gocamp.reservecamping.user.User;
import com.gocamp.reservecamping.user.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/site")
@CrossOrigin(origins = "*")
public class SiteAdminUserController {

    private final SiteAdminUserService service;
    private final UserRepository userRepo;

    public SiteAdminUserController(SiteAdminUserService service, UserRepository userRepo) {
        this.service = service;
        this.userRepo = userRepo;
    }

    private User getCurrentUser(UserDetails userDetails) {
        if (userDetails == null) {
            throw new RuntimeException("Token invalide ou expiré.");
        }

        return userRepo.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Utilisateur connecté introuvable."));
    }

    private void ensureSuperAdmin(User currentUser) {
        if (currentUser.getRole() == null || !"SUPER_ADMIN".equals(currentUser.getRole().getName())) {
            throw new RuntimeException("Accès refusé. Réservé au SUPER_ADMIN.");
        }
    }

    @GetMapping("/users")
    public ResponseEntity<?> getUsers(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            User currentUser = getCurrentUser(userDetails);
            ensureSuperAdmin(currentUser);

            return ResponseEntity.ok(service.getAllUsers());
        } catch (RuntimeException ex) {
            String message = ex.getMessage() != null ? ex.getMessage() : "Erreur serveur";
            if (message.contains("Accès refusé")) {
                return ResponseEntity.status(403).body(Map.of("error", message));
            }
            if (message.contains("Token invalide") || message.contains("expiré")) {
                return ResponseEntity.status(401).body(Map.of("error", message));
            }
            return ResponseEntity.badRequest().body(Map.of("error", message));
        }
    }

    @GetMapping("/roles")
    public ResponseEntity<?> getRoles(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            User currentUser = getCurrentUser(userDetails);
            ensureSuperAdmin(currentUser);

            return ResponseEntity.ok(service.getAllRoles());
        } catch (RuntimeException ex) {
            String message = ex.getMessage() != null ? ex.getMessage() : "Erreur serveur";
            if (message.contains("Accès refusé")) {
                return ResponseEntity.status(403).body(Map.of("error", message));
            }
            if (message.contains("Token invalide") || message.contains("expiré")) {
                return ResponseEntity.status(401).body(Map.of("error", message));
            }
            return ResponseEntity.badRequest().body(Map.of("error", message));
        }
    }

    @PutMapping("/users/{userId}/role")
    public ResponseEntity<?> updateUserRole(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long userId,
            @RequestBody UpdateUserRoleRequest req
    ) {
        try {
            User currentUser = getCurrentUser(userDetails);
            ensureSuperAdmin(currentUser);

            return ResponseEntity.ok(service.updateUserRole(currentUser, userId, req));
        } catch (RuntimeException ex) {
            String message = ex.getMessage() != null ? ex.getMessage() : "Erreur serveur";
            if (message.contains("Accès refusé")) {
                return ResponseEntity.status(403).body(Map.of("error", message));
            }
            if (message.contains("Token invalide") || message.contains("expiré")) {
                return ResponseEntity.status(401).body(Map.of("error", message));
            }
            return ResponseEntity.badRequest().body(Map.of("error", message));
        }
    }
}