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
        Boolean actif
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
                e.getActif()
        );
    }
}