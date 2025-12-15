// packag.woundhealing.repository.UserRepository;
package com.arunnitd.swhdis.swhdis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.arunnitd.swhdis.swhdis.models.Alert;
import com.arunnitd.swhdis.swhdis.repository.AlertRepository;
import com.arunnitd.swhdis.swhdis.repository.BandageRepository;
import com.arunnitd.swhdis.swhdis.repository.PatientRepository;
import com.arunnitd.swhdis.swhdis.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AlertService {

    private final AlertRepository alertRepository;
    private final PatientRepository patientRepository;
    private final BandageRepository bandageRepository;
    private final UserRepository userRepository;

    public List<Alert> getAllAlerts() {
        List<Alert> alerts = alertRepository.findAll();
        alerts.forEach(this::loadRelatedData);
        return alerts;
    }

    public Optional<Alert> getAlertById(Integer id) {
        Optional<Alert> alert = alertRepository.findById(id);
        alert.ifPresent(this::loadRelatedData);
        return alert;
    }

    public List<Alert> getAlertsByPatientId(Integer patientId) {
        List<Alert> alerts = alertRepository.findByPatientId(patientId);
        alerts.forEach(this::loadRelatedData);
        return alerts;
    }

    public List<Alert> getAlertsByBandageId(Integer bandageId) {
        List<Alert> alerts = alertRepository.findByBandageId(bandageId);
        alerts.forEach(this::loadRelatedData);
        return alerts;
    }

    public List<Alert> getAlertsByStatus(Alert.AlertStatus status) {
        List<Alert> alerts = alertRepository.findByStatus(status);
        alerts.forEach(this::loadRelatedData);
        return alerts;
    }

    public List<Alert> getAllActiveAlerts() {
        List<Alert> alerts = alertRepository.findAllActiveAlerts();
        alerts.forEach(this::loadRelatedData);
        return alerts;
    }

    public List<Alert> getActiveAlertsByPatientId(Integer patientId) {
        List<Alert> alerts = alertRepository.findActiveAlertsByPatientId(patientId);
        alerts.forEach(this::loadRelatedData);
        return alerts;
    }

    public List<Alert> getAlertsBySeverity(Alert.Severity severity) {
        List<Alert> alerts = alertRepository.findBySeverity(severity);
        alerts.forEach(this::loadRelatedData);
        return alerts;
    }

    public List<Alert> getActiveCriticalAlerts() {
        List<Alert> alerts = alertRepository.findActiveAlertsBySeverity(Alert.Severity.critical);
        alerts.forEach(this::loadRelatedData);
        return alerts;
    }

    @Transactional
    public Alert createAlert(Alert alert) {
        Alert saved = alertRepository.save(alert);
        loadRelatedData(saved);
        return saved;
    }

    @Transactional
    public Alert acknowledgeAlert(Integer alertId, Integer userId) {
        Alert alert = alertRepository.findById(alertId)
                .orElseThrow(() -> new RuntimeException("Alert not found with id: " + alertId));

        alert.setAcknowledgedBy(userId);
        alert.setAcknowledgedAt(LocalDateTime.now());
        alert.setStatus(Alert.AlertStatus.acknowledged);

        Alert updated = alertRepository.save(alert);
        loadRelatedData(updated);
        return updated;
    }

    @Transactional
    public Alert resolveAlert(Integer alertId) {
        Alert alert = alertRepository.findById(alertId)
                .orElseThrow(() -> new RuntimeException("Alert not found with id: " + alertId));

        alert.setStatus(Alert.AlertStatus.resolved);

        Alert updated = alertRepository.save(alert);
        loadRelatedData(updated);
        return updated;
    }

    @Transactional
    public void deleteAlert(Integer id) {
        Alert alert = alertRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alert not found with id: " + id));
        alertRepository.delete(alert);
    }

    private void loadRelatedData(Alert alert) {
        patientRepository.findById(alert.getPatientId())
                .ifPresent(alert::setPatient);

        bandageRepository.findById(alert.getBandageId())
                .ifPresent(alert::setBandage);

        if (alert.getAcknowledgedBy() != null) {
            userRepository.findById(alert.getAcknowledgedBy())
                    .ifPresent(alert::setAcknowledgedByUser);
        }
    }
}