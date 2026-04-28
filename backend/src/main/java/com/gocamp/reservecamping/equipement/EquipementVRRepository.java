package com.gocamp.reservecamping.equipement;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EquipementVRRepository extends JpaRepository<EquipementVR, Long> {
    List<EquipementVR> findByUserId(Long userId);
}
