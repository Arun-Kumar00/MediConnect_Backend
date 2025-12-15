package com.arunnitd.swhdis.swhdis.repository;

// package com.medical.woundhealing.repository;

// import com.medical.woundhealing.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.arunnitd.swhdis.swhdis.models.Patient;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {

    // Find patients by wound type
    List<Patient> findByWoundType(String woundType);

    // Find patients by last name (case-insensitive)
    List<Patient> findByLastNameContainingIgnoreCase(String lastName);

    // Search in notes using full-text (we'll use simple LIKE for now)
    @Query("SELECT p FROM Patient p WHERE p.notes LIKE %?1%")
    List<Patient> searchInNotes(String keyword);
}