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
                .stream().map(EquipementVRDTO::from).toList();
    }

    public EquipementVRDTO create(Long userId, EquipementVRRequest dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
        EquipementVR eq = new EquipementVR();
        eq.setUser(user);
        mapFromRequest(eq, dto);
        return EquipementVRDTO.from(equipementVRRepository.save(eq));
    }

    public EquipementVRDTO update(Long userId, Long id, EquipementVRRequest dto) {
        EquipementVR eq = equipementVRRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Équipement introuvable"));
        if (!eq.getUser().getId().equals(userId)) {
            throw new RuntimeException("Accès refusé");
        }
        mapFromRequest(eq, dto);
        return EquipementVRDTO.from(equipementVRRepository.save(eq));
    }

    public void delete(Long userId, Long id) {
        EquipementVR eq = equipementVRRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Équipement introuvable"));
        if (!eq.getUser().getId().equals(userId)) {
            throw new RuntimeException("Accès refusé");
        }
        equipementVRRepository.delete(eq);
    }

    private void mapFromRequest(EquipementVR eq, EquipementVRRequest dto) {
        eq.setMarque(dto.getMarque());
        eq.setModele(dto.getModele());
        eq.setLongueur(dto.getLongueur());
        eq.setNoSerie(dto.getNoSerie());
        eq.setNoPlaque(dto.getNoPlaque());
        eq.setHasExtension(dto.getHasExtension() != null && dto.getHasExtension());
        eq.setExtensionConducteur(Boolean.TRUE.equals(dto.getHasExtension()) ? dto.getExtensionConducteur() : null);
        eq.setExtensionPassager(Boolean.TRUE.equals(dto.getHasExtension()) ? dto.getExtensionPassager() : null);
        eq.setActif(dto.getActif() != null ? dto.getActif() : true);
    }
}