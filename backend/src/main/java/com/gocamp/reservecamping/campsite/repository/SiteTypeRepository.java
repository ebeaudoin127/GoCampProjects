package com.gocamp.reservecamping.campsite.repository;

import com.gocamp.reservecamping.campsite.model.SiteType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SiteTypeRepository extends JpaRepository<SiteType, Long> {
    List<SiteType> findByIsActiveTrueOrderByDisplayOrderAscNameFrAsc();
}
