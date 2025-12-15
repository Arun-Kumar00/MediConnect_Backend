package com.arunnitd.swhdis.swhdis.controller;

// package com.medical.woundhealing.controller;

// import com.medical.woundhealing.model.SensorReading;
// import com.medical.woundhealing.service.SensorReadingService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.arunnitd.swhdis.swhdis.models.SensorReading;
import com.arunnitd.swhdis.swhdis.service.SensorReadingService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/readings")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SensorReadingController {

    private final SensorReadingService sensorReadingService;

    @GetMapping
    public ResponseEntity<List<SensorReading>> getAllReadings() {
        return ResponseEntity.ok(sensorReadingService.getAllReadings());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SensorReading> getReadingById(@PathVariable Long id) {
        return sensorReadingService.getReadingById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/bandage/{bandageId}")
    public ResponseEntity<List<SensorReading>> getReadingsByBandageId(@PathVariable Integer bandageId) {
        return ResponseEntity.ok(sensorReadingService.getReadingsByBandageId(bandageId));
    }

    @GetMapping("/bandage/{bandageId}/range")
    public ResponseEntity<List<SensorReading>> getReadingsByTimeRange(
            @PathVariable Integer bandageId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        return ResponseEntity.ok(
                sensorReadingService.getReadingsByBandageIdAndTimeRange(bandageId, startTime, endTime));
    }

    @GetMapping("/bandage/{bandageId}/recent")
    public ResponseEntity<List<SensorReading>> getRecentReadings(
            @PathVariable Integer bandageId,
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(sensorReadingService.getRecentReadings(bandageId, limit));
    }

    @GetMapping("/bandage/{bandageId}/latest")
    public ResponseEntity<SensorReading> getLatestReading(@PathVariable Integer bandageId) {
        SensorReading reading = sensorReadingService.getLatestReading(bandageId);
        if (reading != null) {
            return ResponseEntity.ok(reading);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/bandage/{bandageId}/averages")
    public ResponseEntity<Map<String, Object>> getAverageReadings(@PathVariable Integer bandageId) {
        return ResponseEntity.ok(sensorReadingService.getAverageReadingsLast24Hours(bandageId));
    }

    @GetMapping("/bandage/{bandageId}/count")
    public ResponseEntity<Long> getReadingCount(@PathVariable Integer bandageId) {
        return ResponseEntity.ok(sensorReadingService.getReadingCountByBandage(bandageId));
    }

    @GetMapping("/high-ph")
    public ResponseEntity<List<SensorReading>> getHighPhReadings(
            @RequestParam(defaultValue = "7.8") BigDecimal threshold) {
        return ResponseEntity.ok(sensorReadingService.getReadingsWithHighPh(threshold));
    }

    @GetMapping("/low-oxygen")
    public ResponseEntity<List<SensorReading>> getLowOxygenReadings(
            @RequestParam(defaultValue = "30") BigDecimal threshold) {
        return ResponseEntity.ok(sensorReadingService.getReadingsWithLowOxygen(threshold));
    }

    @PostMapping
    public ResponseEntity<SensorReading> createReading(@RequestBody SensorReading reading) {
        try {
            SensorReading savedReading = sensorReadingService.createReading(reading);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedReading);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<SensorReading>> createBulkReadings(@RequestBody List<SensorReading> readings) {
        try {
            List<SensorReading> savedReadings = sensorReadingService.createBulkReadings(readings);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedReadings);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReading(@PathVariable Long id) {
        try {
            sensorReadingService.deleteReading(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
