package it.aredegalli.auctoritas.repository.audit;

import it.aredegalli.auctoritas.model.audit.AuditEventType;
import it.aredegalli.auctoritas.model.audit.AuditLog;
import it.aredegalli.auctoritas.repository.UUIDRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface AuditLogRepository extends UUIDRepository<AuditLog> {
    List<AuditLog> findByUserId(UUID userId);

    List<AuditLog> findByApplicationName(String applicationName);

    Long deleteByEventTypeAndTimestampBefore(AuditEventType eventType, Instant timestamp);
}
