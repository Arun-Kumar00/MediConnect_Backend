package com.arunnitd.swhdis.swhdis.repository;

// package com.medical.woundhealing.repository;

// import com.medical.woundhealing.model.DrugReleaseEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.arunnitd.swhdis.swhdis.models.DrugReleaseEvent;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DrugReleaseEventRepository extends JpaRepository<DrugReleaseEvent, Integer> {

    List<DrugReleaseEvent> findByBandageId(Integer bandageId);

    List<DrugReleaseEvent> findByDrugName(String drugName);

    List<DrugReleaseEvent> findByAdministeredBy(Integer userId);

    List<DrugReleaseEvent> findByReleaseTimeBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT d FROM DrugReleaseEvent d WHERE d.bandageId = ?1 ORDER BY d.releaseTime DESC")
    List<DrugReleaseEvent> findByBandageIdOrderByReleaseTimeDesc(Integer bandageId);

    Long countByBandageId(Integer bandageId);
}