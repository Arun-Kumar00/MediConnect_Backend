package com.arunnitd.swhdis.swhdis.models;

// package com.medical.woundhealing.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "sensor_readings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SensorReading {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reading_id")
    private Long readingId;

    @Column(name = "bandage_id", nullable = false)
    private Integer bandageId;

    @Column(name = "reading_time", nullable = false)
    private LocalDateTime readingTime;

    @Column(name = "ph_value", precision = 3, scale = 2)
    private BigDecimal phValue;

    @Column(name = "oxygen_percent", precision = 5, scale = 2)
    private BigDecimal oxygenPercent;

    @Column(name = "temperature_celsius", precision = 4, scale = 2)
    private BigDecimal temperatureCelsius;

    @Column(name = "enzyme_index", precision = 6, scale = 2)
    private BigDecimal enzymeIndex;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // Transient field for API response
    @Transient
    private Bandage bandage;
}