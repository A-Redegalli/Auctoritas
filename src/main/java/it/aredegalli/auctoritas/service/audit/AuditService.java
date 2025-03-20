package it.aredegalli.auctoritas.service.audit;

import it.aredegalli.auctoritas.enums.AuditEventTypeEnum;
import it.aredegalli.auctoritas.model.user.User;
import org.springframework.scheduling.annotation.Async;

import java.util.Map;

public interface AuditService {

    @Async
    void logEvent(User user, AuditEventTypeEnum eventEnum, String appName, String description, Map<String, Object> metadata);

    Map<String, Object> buildMetadata(Map<String, Object> metadata);

    Map<String, Object> buildMetadata(AuditEventTypeEnum eventEnum, Map<String, Object> metadata);

    Map<String, Object> buildMetadata(AuditEventTypeEnum eventEnum);
}
