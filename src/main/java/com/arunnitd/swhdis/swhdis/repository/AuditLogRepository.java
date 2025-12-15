package com.arunnitd.swhdis.swhdis.repository;

// package com.medical.woundhealing.repository;

// import com.medical.woundhealing.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.arunnitd.swhdis.swhdis.models.AuditLog;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    List<AuditLog> findByTableName(String tableName);

    List<AuditLog> findByRecordId(Integer recordId);

    List<AuditLog> findByChangedBy(Integer userId);

    List<AuditLog> findByAction(AuditLog.Action action);

    List<AuditLog> findByChangedAtBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT a FROM AuditLog a WHERE a.tableName = ?1 AND a.recordId = ?2 ORDER BY a.changedAt DESC")
    List<AuditLog> findByTableNameAndRecordIdOrderByChangedAtDesc(String tableName, Integer recordId);

    @Query("SELECT a FROM AuditLog a ORDER BY a.changedAt DESC")
    List<AuditLog> findAllOrderByChangedAtDesc();

    void deleteByChangedAtBefore(LocalDateTime cutoffDate);
}
