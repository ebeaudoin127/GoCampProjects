package com.gocamp.reservecamping.campsite.repository;

import com.gocamp.reservecamping.campsite.model.SiteServiceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SiteServiceTypeRepository extends JpaRepository<SiteServiceType, Long> {
    List<SiteServiceType> findByIsActiveTrueOrderByDisplayOrderAscNameFrAsc();
}