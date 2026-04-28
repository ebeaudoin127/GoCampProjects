// ============================================================
// Fichier : SiteAdminUserService.java
// Dernière modification : 2026-04-16
// Auteur : ChatGPT
//
// Résumé :
// - Service du module gestion des utilisateurs
// - Réservé au SUPER_ADMIN
// - Liste les utilisateurs
// - Liste les rôles disponibles
// - Permet de modifier le rôle d'un utilisateur
// ============================================================

package com.gocamp.reservecamping.admin;

import com.gocamp.reservecamping.admin.dto.AdminUserRowResponse;
import com.gocamp.reservecamping.admin.dto.RoleOptionResponse;
import com.gocamp.reservecamping.admin.dto.UpdateUserRoleRequest;
import com.gocamp.reservecamping.role.Role;
import com.gocamp.reservecamping.role.RoleRepository;
import com.gocamp.reservecamping.user.User;
import com.gocamp.reservecamping.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class SiteAdminUserService {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;

    public SiteAdminUserService(UserRepository userRepo, RoleRepository roleRepo) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
    }

    public List<AdminUserRowResponse> getAllUsers() {
        return userRepo.findAll()
                .stream()
                .sorted(Comparator.comparing(User::getId))
                .map(user -> new AdminUserRowResponse(
                        user.getId(),
                        user.getFirstname(),
                        user.getLastname(),
                        user.getEmail(),
                        user.getPhone(),
                        user.getCity(),
                        user.getRole() != null ? user.getRole().getId() : null,
                        user.getRole() != null ? user.getRole().getName() : null
                ))
                .toList();
    }

    public List<RoleOptionResponse> getAllRoles() {
        return roleRepo.findAll()
                .stream()
                .sorted(Comparator.comparing(Role::getId))
                .map(role -> new RoleOptionResponse(role.getId(), role.getName()))
                .toList();
    }

    public AdminUserRowResponse updateUserRole(User currentUser, Long targetUserId, UpdateUserRoleRequest req) {
        if (req == null || req.roleId() == null) {
            throw new RuntimeException("Le roleId est obligatoire.");
        }

        User targetUser = userRepo.findById(targetUserId)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable."));

        Role newRole = roleRepo.findById(req.roleId())
                .orElseThrow(() -> new RuntimeException("Rôle introuvable."));

        // Sécurité : éviter qu’un SUPER_ADMIN retire son propre rôle par erreur
        if (currentUser.getId().equals(targetUser.getId())
                && "SUPER_ADMIN".equals(currentUser.getRole().getName())
                && !"SUPER_ADMIN".equals(newRole.getName())) {
            throw new RuntimeException("Vous ne pouvez pas retirer votre propre rôle SUPER_ADMIN.");
        }

        targetUser.setRole(newRole);
        userRepo.save(targetUser);

        return new AdminUserRowResponse(
                targetUser.getId(),
                targetUser.getFirstname(),
                targetUser.getLastname(),
                targetUser.getEmail(),
                targetUser.getPhone(),
                targetUser.getCity(),
                newRole.getId(),
                newRole.getName()
        );
    }
}