

// ============================================================
// Fichier : EquipmentCompatibilityService.java
// Chemin  : backend/src/main/java/com/gocamp/reservecamping/equipmentcompatibility/service
// Dernière modification : 2026-05-06
// Auteur : ChatGPT pour Eric Beaudoin
//
// Résumé :
// - Validation de compatibilité entre :
//      équipement VR actif de l’utilisateur
//      et campsite réservé
// - Vérifie actuellement :
//      longueur maximale de l’équipement
// - Prépare les validations futures :
//      ampérage
//      services
//      extensions
//
// Historique des modifications :
// 2026-05-06
// - Création initiale du service
// - Correction compilation : utilisation de EquipementVR
// - Correction compilation : utilisation de getMaxEquipmentLengthFeet()
// - Retrait des appels inexistants dans User et Campsite
// ============================================================

package com.gocamp.reservecamping.equipmentcompatibility.service;

import com.gocamp.reservecamping.campsite.model.Campsite;
import com.gocamp.reservecamping.equipement.EquipementVR;
import com.gocamp.reservecamping.equipement.EquipementVRRepository;
import com.gocamp.reservecamping.user.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class EquipmentCompatibilityService {

    private final EquipementVRRepository equipementVRRepository;

    public EquipmentCompatibilityService(
            EquipementVRRepository equipementVRRepository
    ) {
        this.equipementVRRepository = equipementVRRepository;
    }

    // ========================================================
    // VALIDATION COMPATIBILITÉ
    // ========================================================

    public List<String> validateCompatibility(
            User user,
            Campsite campsite
    ) {

        List<String> issues = new ArrayList<>();

        List<EquipementVR> equipements =
                equipementVRRepository.findByUserId(user.getId());

        EquipementVR equipementActif = findEquipementActif(equipements);

        if (equipementActif == null) {
            return issues;
        }

        // ====================================================
        // LONGUEUR MAXIMALE
        // ====================================================

        if (
                equipementActif.getLongueur() != null
                        && campsite.getMaxEquipmentLengthFeet() != null
        ) {

            BigDecimal longueurEquipement =
                    BigDecimal.valueOf(equipementActif.getLongueur());

            if (
                    longueurEquipement.compareTo(
                            campsite.getMaxEquipmentLengthFeet()
                    ) > 0
            ) {

                issues.add(
                        "La longueur de votre équipement dépasse la longueur maximale permise pour ce terrain."
                );
            }
        }

        // ====================================================
        // VALIDATIONS FUTURES
        // ====================================================
        // À ajouter quand les champs seront disponibles dans le modèle :
        //
        // - ampérage requis par l’équipement
        // - services requis : eau / électricité / égout
        // - validation des extensions selon la largeur du site
        // - type d’équipement accepté par le campsite
        // ====================================================

        return issues;
    }

    // ========================================================
    // TROUVER L’ÉQUIPEMENT ACTIF
    // ========================================================

    private EquipementVR findEquipementActif(
            List<EquipementVR> equipements
    ) {

        if (equipements == null || equipements.isEmpty()) {
            return null;
        }

        for (EquipementVR equipement : equipements) {

            if (Boolean.TRUE.equals(equipement.getActif())) {
                return equipement;
            }
        }

        return equipements.get(0);
    }
}