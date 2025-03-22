package it.aredegalli.auctoritas.repository.audit;

import it.aredegalli.auctoritas.model.audit.AuditEventType;
import it.aredegalli.auctoritas.repository.UUIDRepository;

import java.util.Optional;

public interface AuditEventTypeRepository extends UUIDRepository<AuditEventType> {
    Optional<AuditEventType> findByDescription(String description);

}
