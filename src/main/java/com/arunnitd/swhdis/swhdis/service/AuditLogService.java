package com.arunnitd.swhdis.swhdis.service;

// package com.medical.woundhealing.service;

// import com.medical.woundhealing.model.AuditLog;
// import com.medical.woundhealing.repository.AuditLogRepository;
// import com.medical.woundhealing.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.arunnitd.swhdis.swhdis.models.AuditLog;
import com.arunnitd.swhdis.swhdis.repository.AuditLogRepository;
import com.arunnitd.swhdis.swhdis.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepository;

    public List<AuditLog> getAllLogs() {
        List<AuditLog> logs = auditLogRepository.findAllOrderByChangedAtDesc();
        logs.forEach(this::loadUserData);
        return logs;
    }

    public Optional<AuditLog> getLogById(Long id) {
        Optional<AuditLog> log = auditLogRepository.findById(id);
        log.ifPresent(this::loadUserData);
        return log;
    }

    public List<AuditLog> getLogsByTableName(String tableName) {
        List<AuditLog> logs = auditLogRepository.findByTableName(tableName);
        logs.forEach(this::loadUserData);
        return logs;
    }

    public List<AuditLog> getLogsByRecordId(Integer recordId) {
        List<AuditLog> logs = auditLogRepository.findByRecordId(recordId);
        logs.forEach(this::loadUserData);
        return logs;
    }

    public List<AuditLog> getLogsByUser(Integer userId) {
        List<AuditLog> logs = auditLogRepository.findByChangedBy(userId);
        logs.forEach(this::loadUserData);
        return logs;
    }

    public List<AuditLog> getLogsByAction(AuditLog.Action action) {
        List<AuditLog> logs = auditLogRepository.findByAction(action);
        logs.forEach(this::loadUserData);
        return logs;
    }

    public List<AuditLog> getLogsByTimeRange(LocalDateTime start, LocalDateTime end) {
        List<AuditLog> logs = auditLogRepository.findByChangedAtBetween(start, end);
        logs.forEach(this::loadUserData);
        return logs;
    }

    public List<AuditLog> getRecordHistory(String tableName, Integer recordId) {
        List<AuditLog> logs = auditLogRepository.findByTableNameAndRecordIdOrderByChangedAtDesc(tableName, recordId);
        logs.forEach(this::loadUserData);
        return logs;
    }

    @Transactional
    public AuditLog createLog(AuditLog log) {
        AuditLog saved = auditLogRepository.save(log);
        loadUserData(saved);
        return saved;
    }

    @Transactional
    public void deleteOldLogs(LocalDateTime cutoffDate) {
        auditLogRepository.deleteByChangedAtBefore(cutoffDate);
    }

    private void loadUserData(AuditLog log) {
        if (log.getChangedBy() != null) {
            userRepository.findById(log.getChangedBy())
                    .ifPresent(log::setChangedByUser);
        }
    }
}
