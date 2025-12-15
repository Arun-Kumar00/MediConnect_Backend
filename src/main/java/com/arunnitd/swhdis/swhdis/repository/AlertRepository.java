// package com.medical.woundhealing.repository;
package com.arunnitd.swhdis.swhdis.repository;

// import com.medical.woundhealing.model.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.arunnitd.swhdis.swhdis.models.Alert;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Integer> {

    List<Alert> findByPatientId(Integer patientId);

    List<Alert> findByBandageId(Integer bandageId);

    List<Alert> findByStatus(Alert.AlertStatus status);

    List<Alert> findByAlertType(Alert.AlertType alertType);

    List<Alert> findBySeverity(Alert.Severity severity);

    @Query("SELECT a FROM Alert a WHERE a.status = 'active' ORDER BY a.severity DESC, a.triggeredAt DESC")
    List<Alert> findAllActiveAlerts();

    @Query("SELECT a FROM Alert a WHERE a.patientId = ?1 AND a.status = 'active'")
    List<Alert> findActiveAlertsByPatientId(Integer patientId);

    @Query("SELECT a FROM Alert a WHERE a.severity = ?1 AND a.status = 'active'")
    List<Alert> findActiveAlertsBySeverity(Alert.Severity severity);

    Long countByStatusAndSeverity(Alert.AlertStatus status, Alert.Severity severity);

    List<Alert> findByTriggeredAtBetween(LocalDateTime start, LocalDateTime end);
}