package com.gocamp.reservecamping.location;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProvinceStateRepository extends JpaRepository<ProvinceState, Long> {

    // Récupérer toutes les provinces/états selon un pays
    List<ProvinceState> findByCountryId(Long countryId);
}
