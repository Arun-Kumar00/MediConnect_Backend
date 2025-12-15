package com.arunnitd.swhdis.swhdis.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.arunnitd.swhdis.swhdis.models.AuditLog;
import com.arunnitd.swhdis.swhdis.service.AuditLogService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/audit-logs")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuditLogController {

    private final AuditLogService auditLogService;

    @GetMapping
    public ResponseEntity<List<AuditLog>> getAllLogs() {
        return ResponseEntity.ok(auditLogService.getAllLogs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuditLog> getLogById(@PathVariable Long id) {
        return auditLogService.getLogById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/table/{tableName}")
    public ResponseEntity<List<AuditLog>> getLogsByTableName(@PathVariable String tableName) {
        return ResponseEntity.ok(auditLogService.getLogsByTableName(tableName));
    }

    @GetMapping("/record/{recordId}")
    public ResponseEntity<List<AuditLog>> getLogsByRecordId(@PathVariable Integer recordId) {
        return ResponseEntity.ok(auditLogService.getLogsByRecordId(recordId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AuditLog>> getLogsByUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(auditLogService.getLogsByUser(userId));
    }

    @GetMapping("/action/{action}")
    public ResponseEntity<List<AuditLog>> getLogsByAction(@PathVariable AuditLog.Action action) {
        return ResponseEntity.ok(auditLogService.getLogsByAction(action));
    }

    @GetMapping("/range")
    public ResponseEntity<List<AuditLog>> getLogsByTimeRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(auditLogService.getLogsByTimeRange(start, end));
    }

    @GetMapping("/history/{tableName}/{recordId}")
    public ResponseEntity<List<AuditLog>> getRecordHistory(
            @PathVariable String tableName,
            @PathVariable Integer recordId) {
        return ResponseEntity.ok(auditLogService.getRecordHistory(tableName, recordId));
    }

    @PostMapping
    public ResponseEntity<AuditLog> createLog(@RequestBody AuditLog log) {
        AuditLog savedLog = auditLogService.createLog(log);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedLog);
    }
}
