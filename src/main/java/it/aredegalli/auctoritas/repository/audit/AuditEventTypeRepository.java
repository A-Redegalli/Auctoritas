package it.aredegalli.auctoritas.repository.audit;

import it.aredegalli.auctoritas.model.audit.AuditEventType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AuditEventTypeRepository extends JpaRepository<AuditEventType, UUID> {
    Optional<AuditEventType> findByDescription(String description);

}
