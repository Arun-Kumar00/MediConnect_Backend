package com.arunnitd.swhdis.swhdis.service;

// public class x {

// }
// package com.medical.woundhealing.service;

// import com.medical.woundhealing.model.SensorReading;
// import com.medical.woundhealing.repository.BandageRepository;
// import com.medical.woundhealing.repository.SensorReadingRepository;
import lombok.RequiredArgsConstructor;
// import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.arunnitd.swhdis.swhdis.models.SensorReading;
import com.arunnitd.swhdis.swhdis.repository.BandageRepository;
import com.arunnitd.swhdis.swhdis.repository.SensorReadingRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SensorReadingService {

    private final SensorReadingRepository sensorReadingRepository;
    private final BandageRepository bandageRepository;

    public List<SensorReading> getAllReadings() {
        return sensorReadingRepository.findAll();
    }

    public Optional<SensorReading> getReadingById(Long id) {
        Optional<SensorReading> reading = sensorReadingRepository.findById(id);
        reading.ifPresent(this::loadBandageData);
        return reading;
    }

    public List<SensorReading> getReadingsByBandageId(Integer bandageId) {
        List<SensorReading> readings = sensorReadingRepository.findByBandageId(bandageId);
        readings.forEach(this::loadBandageData);
        return readings;
    }

    public List<SensorReading> getReadingsByBandageIdAndTimeRange(
            Integer bandageId, LocalDateTime startTime, LocalDateTime endTime) {
        List<SensorReading> readings = sensorReadingRepository
                .findByBandageIdAndTimeRange(bandageId, startTime, endTime);
        readings.forEach(this::loadBandageData);
        return readings;
    }

    public List<SensorReading> getRecentReadings(Integer bandageId, int limit) {
        List<SensorReading> readings = sensorReadingRepository
                .findRecentReadingsByBandageId(bandageId);
        List<SensorReading> limitedReadings = readings.stream()
                .limit(limit)
                .toList();
        limitedReadings.forEach(this::loadBandageData);
        return limitedReadings;
    }

    public SensorReading getLatestReading(Integer bandageId) {
        SensorReading reading = sensorReadingRepository.findLatestReadingByBandageId(bandageId);
        if (reading != null) {
            loadBandageData(reading);
        }
        return reading;
    }

    public List<SensorReading> getReadingsWithHighPh(BigDecimal phThreshold) {
        return sensorReadingRepository.findReadingsWithHighPh(phThreshold);
    }

    public List<SensorReading> getReadingsWithLowOxygen(BigDecimal oxygenThreshold) {
        return sensorReadingRepository.findReadingsWithLowOxygen(oxygenThreshold);
    }

    public Map<String, Object> getAverageReadingsLast24Hours(Integer bandageId) {
        LocalDateTime since = LocalDateTime.now().minusHours(24);
        Object[] result = sensorReadingRepository.getAverageReadings(bandageId, since);

        Map<String, Object> averages = new HashMap<>();
        if (result != null && result.length > 0) {
            averages.put("avgPh", result[0]);
            averages.put("avgOxygen", result[1]);
            averages.put("avgTemperature", result[2]);
            averages.put("avgEnzyme", result[3]);
        }
        return averages;
    }

    public Long getReadingCountByBandage(Integer bandageId) {
        return sensorReadingRepository.countByBandageId(bandageId);
    }

    @Transactional
    public SensorReading createReading(SensorReading reading) {
        // Validate bandage exists
        if (!bandageRepository.existsById(reading.getBandageId())) {
            throw new RuntimeException("Bandage not found with id: " + reading.getBandageId());
        }

        // Validate sensor values
        validateSensorValues(reading);

        SensorReading saved = sensorReadingRepository.save(reading);
        loadBandageData(saved);
        return saved;
    }

    @Transactional
    public List<SensorReading> createBulkReadings(List<SensorReading> readings) {
        // Validate all readings
        readings.forEach(this::validateSensorValues);

        List<SensorReading> savedReadings = sensorReadingRepository.saveAll(readings);
        savedReadings.forEach(this::loadBandageData);
        return savedReadings;
    }

    @Transactional
    public void deleteReading(Long id) {
        SensorReading reading = sensorReadingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reading not found with id: " + id));
        sensorReadingRepository.delete(reading);
    }

    @Transactional
    public void deleteOldReadings(LocalDateTime cutoffDate) {
        sensorReadingRepository.deleteByReadingTimeBefore(cutoffDate);
    }

    // Helper method to load bandage data
    private void loadBandageData(SensorReading reading) {
        bandageRepository.findById(reading.getBandageId())
                .ifPresent(reading::setBandage);
    }

    // Helper method to validate sensor values
    private void validateSensorValues(SensorReading reading) {
        // Validate pH (0-14)
        if (reading.getPhValue() != null) {
            if (reading.getPhValue().compareTo(BigDecimal.ZERO) < 0 ||
                    reading.getPhValue().compareTo(new BigDecimal("14")) > 0) {
                throw new RuntimeException("pH value must be between 0 and 14");
            }
        }

        // Validate oxygen (0-100%)
        if (reading.getOxygenPercent() != null) {
            if (reading.getOxygenPercent().compareTo(BigDecimal.ZERO) < 0 ||
                    reading.getOxygenPercent().compareTo(new BigDecimal("100")) > 0) {
                throw new RuntimeException("Oxygen percent must be between 0 and 100");
            }
        }

        // Validate temperature (20-45°C)
        if (reading.getTemperatureCelsius() != null) {
            if (reading.getTemperatureCelsius().compareTo(new BigDecimal("20")) < 0 ||
                    reading.getTemperatureCelsius().compareTo(new BigDecimal("45")) > 0) {
                throw new RuntimeException("Temperature must be between 20 and 45°C");
            }
        }
    }
}