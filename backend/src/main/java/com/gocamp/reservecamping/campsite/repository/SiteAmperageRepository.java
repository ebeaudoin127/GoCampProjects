package com.gocamp.reservecamping.campsite.repository;

import com.gocamp.reservecamping.campsite.model.SiteAmperage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SiteAmperageRepository extends JpaRepository<SiteAmperage, Long> {
    List<SiteAmperage> findByIsActiveTrueOrderByDisplayOrderAscNameFrAsc();
}
