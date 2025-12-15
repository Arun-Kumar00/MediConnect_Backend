package com.arunnitd.swhdis.swhdis.repository;

// package com.medical.woundhealing.repository;

// import com.medical.woundhealing.model.SensorReading;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.arunnitd.swhdis.swhdis.models.SensorReading;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SensorReadingRepository extends JpaRepository<SensorReading, Long> {

    // Find all readings for a specific bandage
    List<SensorReading> findByBandageId(Integer bandageId);

    // Find readings for a bandage within a time range
    @Query("SELECT sr FROM SensorReading sr WHERE sr.bandageId = ?1 AND sr.readingTime BETWEEN ?2 AND ?3 ORDER BY sr.readingTime DESC")
    List<SensorReading> findByBandageIdAndTimeRange(Integer bandageId, LocalDateTime startTime, LocalDateTime endTime);

    // Find recent readings for a bandage (last N readings)
    @Query("SELECT sr FROM SensorReading sr WHERE sr.bandageId = ?1 ORDER BY sr.readingTime DESC")
    List<SensorReading> findRecentReadingsByBandageId(Integer bandageId);

    // Find readings with high pH (potential infection)
    @Query("SELECT sr FROM SensorReading sr WHERE sr.phValue > ?1 ORDER BY sr.readingTime DESC")
    List<SensorReading> findReadingsWithHighPh(BigDecimal phThreshold);

    // Find readings with low oxygen
    @Query("SELECT sr FROM SensorReading sr WHERE sr.oxygenPercent < ?1 ORDER BY sr.readingTime DESC")
    List<SensorReading> findReadingsWithLowOxygen(BigDecimal oxygenThreshold);

    // Get average sensor values for a bandage in last 24 hours
    @Query("SELECT AVG(sr.phValue) as avgPh, AVG(sr.oxygenPercent) as avgO2, " +
            "AVG(sr.temperatureCelsius) as avgTemp, AVG(sr.enzymeIndex) as avgEnzyme " +
            "FROM SensorReading sr WHERE sr.bandageId = :bandageId " +
            "AND sr.readingTime > :since")
    Object[] getAverageReadings(@Param("bandageId") Integer bandageId, @Param("since") LocalDateTime since);

    // Get latest reading for a bandage
    @Query("SELECT sr FROM SensorReading sr WHERE sr.bandageId = ?1 ORDER BY sr.readingTime DESC LIMIT 1")
    SensorReading findLatestReadingByBandageId(Integer bandageId);

    // Count readings for a bandage
    Long countByBandageId(Integer bandageId);

    // Delete old readings (for archival)
    void deleteByReadingTimeBefore(LocalDateTime cutoffDate);
}