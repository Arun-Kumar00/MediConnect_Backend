package com.arunnitd.swhdis.swhdis.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "alerts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alert_id")
    private Integer alertId;

    @Column(name = "bandage_id", nullable = false)
    private Integer bandageId;

    @Column(name = "patient_id", nullable = false)
    private Integer patientId;

    @Enumerated(EnumType.STRING)
    @Column(name = "alert_type", nullable = false)
    private AlertType alertType;

    @Enumerated(EnumType.STRING)
    @Column(name = "severity", nullable = false)
    private Severity severity;

    @Column(name = "message", nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(name = "triggered_at", nullable = false)
    private LocalDateTime triggeredAt;

    @Column(name = "acknowledged_by")
    private Integer acknowledgedBy;

    @Column(name = "acknowledged_at")
    private LocalDateTime acknowledgedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AlertStatus status = AlertStatus.active;

    // Transient fields
    @Transient
    private Patient patient;

    @Transient
    private Bandage bandage;

    @Transient
    private User acknowledgedByUser;

    public enum AlertType {
        high_ph, low_oxygen, high_temp, infection_risk, enzyme_spike
    }

    public enum Severity {
        low, medium, high, critical
    }

    public enum AlertStatus {
        active, acknowledged, resolved
    }
}