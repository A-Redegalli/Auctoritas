package it.aredegalli.auctoritas.service.audit;

import it.aredegalli.auctoritas.enums.AuditEventTypeEnum;
import it.aredegalli.auctoritas.model.audit.AuditEventType;
import it.aredegalli.auctoritas.model.audit.AuditLog;
import it.aredegalli.auctoritas.model.user.User;
import it.aredegalli.auctoritas.repository.audit.AuditEventTypeRepository;
import it.aredegalli.auctoritas.repository.audit.AuditLogRepository;
import it.aredegalli.auctoritas.util.RequestUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {
    private final AuditLogRepository auditLogRepository;
    private final AuditEventTypeRepository eventTypeRepository;
    private final HttpServletRequest httpServletRequest;

    @Async
    @Override
    public void logEvent(User user, AuditEventTypeEnum eventEnum, String appName, String description, Map<String, Object> metadata) {
        AuditEventType eventType = eventTypeRepository.findByDescription(eventEnum.name())
                .orElseGet(() -> eventTypeRepository.save(AuditEventType.builder().description(eventEnum.name()).build()));

        AuditLog audit = AuditLog.builder()
                .user(user)
                .eventType(eventType)
                .applicationName(appName)
                .description(description)
                .metadata(metadata)
                .timestamp(Instant.now())
                .build();

        log.debug("[AUDIT] Audit event: {}", audit);

        auditLogRepository.save(audit);
    }

    @Override
    public Map<String, Object> buildMetadata(Map<String, Object> metadata) {
        metadata.put("ipv4", RequestUtil.getClientIp(httpServletRequest));
        metadata.put("user-agent", httpServletRequest.getHeader("User-Agent"));

        return metadata;
    }

    @Override
    public Map<String, Object> buildMetadata(AuditEventTypeEnum eventEnum, Map<String, Object> metadata) {
        Map<String, Object> map = this.buildMetadata(metadata);
        map.put("event", eventEnum.name());
        return map;
    }

    @Override
    public Map<String, Object> buildMetadata(AuditEventTypeEnum eventEnum) {
        return this.buildMetadata(eventEnum, Map.of());
    }

}
