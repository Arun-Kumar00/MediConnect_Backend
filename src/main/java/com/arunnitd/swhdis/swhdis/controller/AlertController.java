package com.arunnitd.swhdis.swhdis.controller;

// woundhealing.service.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.arunnitd.swhdis.swhdis.models.Alert;
import com.arunnitd.swhdis.swhdis.service.AlertService;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AlertController {

    private final AlertService alertService;

    @GetMapping
    public ResponseEntity<List<Alert>> getAllAlerts() {
        return ResponseEntity.ok(alertService.getAllAlerts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Alert> getAlertById(@PathVariable Integer id) {
        return alertService.getAlertById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Alert>> getAlertsByPatientId(@PathVariable Integer patientId) {
        return ResponseEntity.ok(alertService.getAlertsByPatientId(patientId));
    }

    @GetMapping("/bandage/{bandageId}")
    public ResponseEntity<List<Alert>> getAlertsByBandageId(@PathVariable Integer bandageId) {
        return ResponseEntity.ok(alertService.getAlertsByBandageId(bandageId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Alert>> getAlertsByStatus(@PathVariable Alert.AlertStatus status) {
        return ResponseEntity.ok(alertService.getAlertsByStatus(status));
    }

    @GetMapping("/active")
    public ResponseEntity<List<Alert>> getAllActiveAlerts() {
        return ResponseEntity.ok(alertService.getAllActiveAlerts());
    }

    @GetMapping("/patient/{patientId}/active")
    public ResponseEntity<List<Alert>> getActiveAlertsByPatientId(@PathVariable Integer patientId) {
        return ResponseEntity.ok(alertService.getActiveAlertsByPatientId(patientId));
    }

    @GetMapping("/severity/{severity}")
    public ResponseEntity<List<Alert>> getAlertsBySeverity(@PathVariable Alert.Severity severity) {
        return ResponseEntity.ok(alertService.getAlertsBySeverity(severity));
    }

    @GetMapping("/critical")
    public ResponseEntity<List<Alert>> getActiveCriticalAlerts() {
        return ResponseEntity.ok(alertService.getActiveCriticalAlerts());
    }

    @PostMapping
    public ResponseEntity<Alert> createAlert(@RequestBody Alert alert) {
        Alert savedAlert = alertService.createAlert(alert);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAlert);
    }

    @PatchMapping("/{id}/acknowledge")
    public ResponseEntity<Alert> acknowledgeAlert(
            @PathVariable Integer id,
            @RequestParam Integer userId) {
        try {
            Alert acknowledgedAlert = alertService.acknowledgeAlert(id, userId);
            return ResponseEntity.ok(acknowledgedAlert);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/resolve")
    public ResponseEntity<Alert> resolveAlert(@PathVariable Integer id) {
        try {
            Alert resolvedAlert = alertService.resolveAlert(id);
            return ResponseEntity.ok(resolvedAlert);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlert(@PathVariable Integer id) {
        try {
            alertService.deleteAlert(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}