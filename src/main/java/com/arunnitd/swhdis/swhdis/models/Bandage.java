package com.arunnitd.swhdis.swhdis.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "bandages")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bandage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bandage_id")
    private Integer bandageId;

    @Column(name = "bandage_serial", nullable = false, unique = true, length = 50)
    private String bandageSerial;

    @Column(name = "patient_id", nullable = false)
    private Integer patientId;

    @Column(name = "device_id", nullable = false)
    private Integer deviceId;

    @Column(name = "application_date", nullable = false)
    private LocalDateTime applicationDate;

    @Column(name = "removal_date")
    private LocalDateTime removalDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BandageStatus status = BandageStatus.active;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // Transient fields for API response (not stored in DB)
    @Transient
    private Patient patient;

    @Transient
    private Device device;

    public enum BandageStatus {
        active, removed, expired
    }
}