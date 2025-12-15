package com.arunnitd.swhdis.swhdis.models;

// package com.medical.woundhealing.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "devices")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "device_id")
    private Integer deviceId;

    @Column(name = "device_serial", nullable = false, unique = true, length = 50)
    private String deviceSerial;

    @Column(name = "device_model", nullable = false, length = 50)
    private String deviceModel;

    @Column(name = "manufacture_date")
    private LocalDate manufactureDate;

    @Column(name = "last_calibration")
    private LocalDateTime lastCalibration;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private DeviceStatus status = DeviceStatus.active;

    @Column(name = "firmware_version", length = 20)
    private String firmwareVersion;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public enum DeviceStatus {
        active, maintenance, retired
    }
}