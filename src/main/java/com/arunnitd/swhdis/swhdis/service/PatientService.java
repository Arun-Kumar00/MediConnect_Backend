package com.arunnitd.swhdis.swhdis.service;

// package com.medical.woundhealing.service;

// import com.medical.woundhealing.model.Patient;
// import com.medical.woundhealing.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.arunnitd.swhdis.swhdis.models.Patient;
import com.arunnitd.swhdis.swhdis.repository.PatientRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    // Get all patients
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    // Get patient by ID
    public Optional<Patient> getPatientById(Integer id) {
        return patientRepository.findById(id);
    }

    // Create new patient
    @Transactional
    public Patient createPatient(Patient patient) {
        return patientRepository.save(patient);
    }

    // Update existing patient
    @Transactional
    public Patient updatePatient(Integer id, Patient patientDetails) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));

        patient.setFirstName(patientDetails.getFirstName());
        patient.setLastName(patientDetails.getLastName());
        patient.setDateOfBirth(patientDetails.getDateOfBirth());
        patient.setGender(patientDetails.getGender());
        patient.setContactNumber(patientDetails.getContactNumber());
        patient.setEmail(patientDetails.getEmail());
        patient.setWoundType(patientDetails.getWoundType());
        patient.setWoundLocation(patientDetails.getWoundLocation());
        patient.setWoundSizeCm2(patientDetails.getWoundSizeCm2());
        patient.setNotes(patientDetails.getNotes());

        return patientRepository.save(patient);
    }

    // Delete patient
    @Transactional
    public void deletePatient(Integer id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));
        patientRepository.delete(patient);
    }

    // Search patients by wound type
    public List<Patient> getPatientsByWoundType(String woundType) {
        return patientRepository.findByWoundType(woundType);
    }

    // Search patients by last name
    public List<Patient> searchPatientsByLastName(String lastName) {
        return patientRepository.findByLastNameContainingIgnoreCase(lastName);
    }

    // Search in patient notes
    public List<Patient> searchInNotes(String keyword) {
        return patientRepository.searchInNotes(keyword);
    }
}
