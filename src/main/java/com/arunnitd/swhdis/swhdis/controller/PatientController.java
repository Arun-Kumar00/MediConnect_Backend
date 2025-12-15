package com.arunnitd.swhdis.swhdis.controller;

// package com.medical.woundhealing.controller;

// import com.medical.woundhealing.model.Patient;
// import com.medical.woundhealing.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.arunnitd.swhdis.swhdis.models.Patient;
import com.arunnitd.swhdis.swhdis.service.PatientService;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Allow frontend to access
public class PatientController {

    private final PatientService patientService;

    // GET all patients
    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients() {
        List<Patient> patients = patientService.getAllPatients();
        return ResponseEntity.ok(patients);
    }

    // GET patient by ID
    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Integer id) {
        return patientService.getPatientById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST create new patient
    @PostMapping
    public ResponseEntity<Patient> createPatient(@RequestBody Patient patient) {
        Patient savedPatient = patientService.createPatient(patient);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPatient);
    }

    // PUT update patient
    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(
            @PathVariable Integer id,
            @RequestBody Patient patientDetails) {
        try {
            Patient updatedPatient = patientService.updatePatient(id, patientDetails);
            return ResponseEntity.ok(updatedPatient);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE patient
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Integer id) {
        try {
            patientService.deletePatient(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // GET patients by wound type
    @GetMapping("/wound-type/{woundType}")
    public ResponseEntity<List<Patient>> getPatientsByWoundType(@PathVariable String woundType) {
        List<Patient> patients = patientService.getPatientsByWoundType(woundType);
        return ResponseEntity.ok(patients);
    }

    // GET search patients by last name
    @GetMapping("/search")
    public ResponseEntity<List<Patient>> searchPatients(@RequestParam String lastName) {
        List<Patient> patients = patientService.searchPatientsByLastName(lastName);
        return ResponseEntity.ok(patients);
    }

    // GET search in patient notes
    @GetMapping("/search-notes")
    public ResponseEntity<List<Patient>> searchInNotes(@RequestParam String keyword) {
        List<Patient> patients = patientService.searchInNotes(keyword);
        return ResponseEntity.ok(patients);
    }
}
