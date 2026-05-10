






// ============================================================
// Fichier : EquipementVRService.java
// Chemin  : backend/src/main/java/com/gocamp/reservecamping/equipement
// Dernière modification : 2026-05-09
// Auteur : ChatGPT pour Eric Beaudoin
//
// Résumé :
// - Service métier des équipements VR
// - Création, modification, suppression et consultation
// - Ajout des options de recherche par défaut
//
// Historique des modifications :
// 2026-05-09
// - Ajout mapping defaultRequiresWater
// - Ajout mapping defaultRequiresElectricity
// - Ajout mapping defaultRequiresSewer
// - Ajout mapping defaultRequires15_20Amp
// - Ajout mapping defaultRequires30Amp
// - Ajout mapping defaultRequires50Amp
// ============================================================

package com.gocamp.reservecamping.equipement;

import com.gocamp.reservecamping.equipement.dto.EquipementVRDTO;
import com.gocamp.reservecamping.equipement.dto.EquipementVRRequest;
import com.gocamp.reservecamping.user.User;
import com.gocamp.reservecamping.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipementVRService {

    @Autowired
    private EquipementVRRepository equipementVRRepository;

    @Autowired
    private UserRepository userRepository;

    public List<EquipementVRDTO> getByUser(Long userId) {
        return equipementVRRepository.findByUserId(userId)
                .stream()
                .map(EquipementVRDTO::from)
                .toList();
    }

    public EquipementVRDTO create(
            Long userId,
            EquipementVRRequest dto
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        EquipementVR eq = new EquipementVR();

        eq.setUser(user);

        mapFromRequest(eq, dto);

        return EquipementVRDTO.from(
                equipementVRRepository.save(eq)
        );
    }

    public EquipementVRDTO update(
            Long userId,
            Long id,
            EquipementVRRequest dto
    ) {
        EquipementVR eq = equipementVRRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Équipement introuvable"));

        if (!eq.getUser().getId().equals(userId)) {
            throw new RuntimeException("Accès refusé");
        }

        mapFromRequest(eq, dto);

        return EquipementVRDTO.from(
                equipementVRRepository.save(eq)
        );
    }

    public void delete(
            Long userId,
            Long id
    ) {
        EquipementVR eq = equipementVRRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Équipement introuvable"));

        if (!eq.getUser().getId().equals(userId)) {
            throw new RuntimeException("Accès refusé");
        }

        equipementVRRepository.delete(eq);
    }

    private void mapFromRequest(
            EquipementVR eq,
            EquipementVRRequest dto
    ) {
        eq.setMarque(dto.getMarque());
        eq.setModele(dto.getModele());
        eq.setLongueur(dto.getLongueur());
        eq.setNoSerie(dto.getNoSerie());
        eq.setNoPlaque(dto.getNoPlaque());

        eq.setHasExtension(
                dto.getHasExtension() != null && dto.getHasExtension()
        );

        eq.setExtensionConducteur(
                Boolean.TRUE.equals(dto.getHasExtension())
                        ? dto.getExtensionConducteur()
                        : null
        );

        eq.setExtensionPassager(
                Boolean.TRUE.equals(dto.getHasExtension())
                        ? dto.getExtensionPassager()
                        : null
        );

        eq.setActif(
                dto.getActif() != null ? dto.getActif() : true
        );

        eq.setDefaultRequiresWater(
                Boolean.TRUE.equals(dto.getDefaultRequiresWater())
        );

        eq.setDefaultRequiresElectricity(
                Boolean.TRUE.equals(dto.getDefaultRequiresElectricity())
        );

        eq.setDefaultRequiresSewer(
                Boolean.TRUE.equals(dto.getDefaultRequiresSewer())
        );

        eq.setDefaultRequires15_20Amp(
                Boolean.TRUE.equals(dto.getDefaultRequires15_20Amp())
        );

        eq.setDefaultRequires30Amp(
                Boolean.TRUE.equals(dto.getDefaultRequires30Amp())
        );

        eq.setDefaultRequires50Amp(
                Boolean.TRUE.equals(dto.getDefaultRequires50Amp())
        );
    }
}


