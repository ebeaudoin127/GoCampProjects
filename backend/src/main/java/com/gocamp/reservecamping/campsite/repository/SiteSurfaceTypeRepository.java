package com.gocamp.reservecamping.campsite.repository;

import com.gocamp.reservecamping.campsite.model.SiteSurfaceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SiteSurfaceTypeRepository extends JpaRepository<SiteSurfaceType, Long> {
    List<SiteSurfaceType> findByIsActiveTrueOrderByDisplayOrderAscNameFrAsc();
}