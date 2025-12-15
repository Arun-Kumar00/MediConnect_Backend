package com.arunnitd.swhdis.swhdis.controller;

// package com.medical.woundhealing.controller;

// import com.medical.woundhealing.model.Bandage;
// import com.medical.woundhealing.service.BandageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.arunnitd.swhdis.swhdis.models.Bandage;
import com.arunnitd.swhdis.swhdis.service.BandageService;

import java.util.List;

@RestController
@RequestMapping("/api/bandages")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BandageController {

    private final BandageService bandageService;

    @GetMapping
    public ResponseEntity<List<Bandage>> getAllBandages() {
        return ResponseEntity.ok(bandageService.getAllBandages());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bandage> getBandageById(@PathVariable Integer id) {
        return bandageService.getBandageById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/serial/{serial}")
    public ResponseEntity<Bandage> getBandageBySerial(@PathVariable String serial) {
        return bandageService.getBandageBySerial(serial)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Bandage>> getBandagesByPatientId(@PathVariable Integer patientId) {
        return ResponseEntity.ok(bandageService.getBandagesByPatientId(patientId));
    }

    @GetMapping("/device/{deviceId}")
    public ResponseEntity<List<Bandage>> getBandagesByDeviceId(@PathVariable Integer deviceId) {
        return ResponseEntity.ok(bandageService.getBandagesByDeviceId(deviceId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Bandage>> getBandagesByStatus(@PathVariable Bandage.BandageStatus status) {
        return ResponseEntity.ok(bandageService.getBandagesByStatus(status));
    }

    @GetMapping("/active")
    public ResponseEntity<List<Bandage>> getActiveBandages() {
        return ResponseEntity.ok(bandageService.getActiveBandages());
    }

    @PostMapping
    public ResponseEntity<Bandage> createBandage(@RequestBody Bandage bandage) {
        try {
            Bandage savedBandage = bandageService.createBandage(bandage);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedBandage);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Bandage> updateBandage(@PathVariable Integer id, @RequestBody Bandage bandageDetails) {
        try {
            Bandage updatedBandage = bandageService.updateBandage(id, bandageDetails);
            return ResponseEntity.ok(updatedBandage);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/remove")
    public ResponseEntity<Bandage> removeBandage(@PathVariable Integer id) {
        try {
            Bandage removedBandage = bandageService.removeBandage(id);
            return ResponseEntity.ok(removedBandage);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBandage(@PathVariable Integer id) {
        try {
            bandageService.deleteBandage(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
