package it.aredegalli.auctoritas.repository.audit;

import it.aredegalli.auctoritas.model.audit.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {
    List<AuditLog> findByUserId(UUID userId);

    List<AuditLog> findByApplicationName(String applicationName);
}
