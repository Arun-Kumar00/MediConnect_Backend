package com.arunnitd.swhdis.swhdis.repository;

// package com.medical.woundhealing.repository;

// import com.medical.woundhealing.model.Bandage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.arunnitd.swhdis.swhdis.models.Bandage;

import java.util.List;
import java.util.Optional;

@Repository
public interface BandageRepository extends JpaRepository<Bandage, Integer> {

    Optional<Bandage> findByBandageSerial(String bandageSerial);

    List<Bandage> findByPatientId(Integer patientId);

    List<Bandage> findByDeviceId(Integer deviceId);

    List<Bandage> findByStatus(Bandage.BandageStatus status);

    // Find active bandages for a specific patient
    @Query("SELECT b FROM Bandage b WHERE b.patientId = ?1 AND b.status = 'active'")
    List<Bandage> findActiveBandagesByPatientId(Integer patientId);

    // Find all active bandages
    @Query("SELECT b FROM Bandage b WHERE b.status = 'active'")
    List<Bandage> findAllActiveBandages();
}