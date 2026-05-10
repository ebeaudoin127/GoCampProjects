// ============================================================
// Fichier : EquipementVRDTO.java
// Chemin  : backend/src/main/java/com/gocamp/reservecamping/equipement/dto
// Dernière modification : 2026-05-09
// Auteur : ChatGPT pour Eric Beaudoin
//
// Résumé :
// - DTO de réponse pour un équipement VR
// - Ajout des options de recherche par défaut
//
// Historique des modifications :
// 2026-05-09
// - Ajout defaultRequiresWater
// - Ajout defaultRequiresElectricity
// - Ajout defaultRequiresSewer
// - Ajout defaultRequires15_20Amp
// - Ajout defaultRequires30Amp
// - Ajout defaultRequires50Amp
// ============================================================

package com.gocamp.reservecamping.equipement.dto;

import com.gocamp.reservecamping.equipement.EquipementVR;

public record EquipementVRDTO(
        Long id,
        String marque,
        String modele,
        Integer longueur,
        String noSerie,
        String noPlaque,
        Boolean hasExtension,
        Integer extensionConducteur,
        Integer extensionPassager,
        Boolean actif,

        Boolean defaultRequiresWater,
        Boolean defaultRequiresElectricity,
        Boolean defaultRequiresSewer,
        Boolean defaultRequires15_20Amp,
        Boolean defaultRequires30Amp,
        Boolean defaultRequires50Amp
) {
    public static EquipementVRDTO from(EquipementVR e) {
        return new EquipementVRDTO(
                e.getId(),
                e.getMarque(),
                e.getModele(),
                e.getLongueur(),
                e.getNoSerie(),
                e.getNoPlaque(),
                e.getHasExtension(),
                e.getExtensionConducteur(),
                e.getExtensionPassager(),
                e.getActif(),

                e.getDefaultRequiresWater(),
                e.getDefaultRequiresElectricity(),
                e.getDefaultRequiresSewer(),
                e.getDefaultRequires15_20Amp(),
                e.getDefaultRequires30Amp(),
                e.getDefaultRequires50Amp()
        );
    }
}
