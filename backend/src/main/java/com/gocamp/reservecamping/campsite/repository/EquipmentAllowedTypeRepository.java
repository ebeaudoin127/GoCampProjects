package com.gocamp.reservecamping.campsite.repository;

import com.gocamp.reservecamping.campsite.model.EquipmentAllowedType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EquipmentAllowedTypeRepository extends JpaRepository<EquipmentAllowedType, Long> {
    List<EquipmentAllowedType> findByIsActiveTrueOrderByDisplayOrderAscNameFrAsc();
}
