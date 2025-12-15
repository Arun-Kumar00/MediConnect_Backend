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
@Table(name = "drug_release_events")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DrugReleaseEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Integer eventId;

    @Column(name = "bandage_id", nullable = false)
    private Integer bandageId;

    @Column(name = "release_time", nullable = false)
    private LocalDateTime releaseTime;

    @Column(name = "drug_name", nullable = false, length = 100)
    private String drugName;

    @Column(name = "dosage_mg", nullable = false, precision = 8, scale = 2)
    private BigDecimal dosageMg;

    @Column(name = "trigger_reason", length = 255)
    private String triggerReason;

    @Column(name = "administered_by")
    private Integer administeredBy;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // Transient fields
    @Transient
    private Bandage bandage;

    @Transient
    private User administeredByUser;
}
