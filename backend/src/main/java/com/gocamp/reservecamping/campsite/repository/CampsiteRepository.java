package com.gocamp.reservecamping.campsite.repository;

import com.gocamp.reservecamping.campsite.model.Campsite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CampsiteRepository extends JpaRepository<Campsite, Long> {

    List<Campsite> findByCampgroundIdOrderBySiteCodeAsc(Long campgroundId);

    Optional<Campsite> findByCampgroundIdAndSiteCode(Long campgroundId, String siteCode);
}
