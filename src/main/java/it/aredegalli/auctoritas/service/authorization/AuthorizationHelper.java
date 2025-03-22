package it.aredegalli.auctoritas.service.authorization;

import it.aredegalli.auctoritas.enums.AuditEventTypeEnum;
import it.aredegalli.auctoritas.model.role.Role;
import it.aredegalli.auctoritas.model.user.User;
import it.aredegalli.auctoritas.service.audit.AuditService;
import it.aredegalli.auctoritas.service.validation.annotation.EntityExistence;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthorizationHelper {

    private final AuditService auditService;

    public void logAccessGranted(User user, String appName, String description, Map<String, Object> metadata) {
        log.info("[AUTH] ACCESS GRANTED: user={} app={} desc={} meta={}",
                user != null ? user.getId() : "anonymous", appName, description, metadata);
        auditService.logEvent(user, AuditEventTypeEnum.ACCESS_GRANTED, appName, description, metadata);
    }

    public void logAccessDenied(User user, String appName, String description, Map<String, Object> metadata) {
        log.warn("[AUTH] ACCESS DENIED: user={} app={} desc={} meta={}",
                user != null ? user.getId() : "anonymous", appName, description, metadata);
        auditService.logEvent(user, AuditEventTypeEnum.ACCESS_DENIED, appName, description, metadata);
    }

    @EntityExistence(repository = it.aredegalli.auctoritas.repository.application.ApplicationRepository.class, idParam = "name", method = "existsByName")
    public void validateApplicationExists(String name) {
        log.debug("[VALIDATION] Application exists: {}", name);
    }

    @EntityExistence(repository = it.aredegalli.auctoritas.repository.authenticator.AuthenticatorRepository.class, idParam = "name", method = "existsByName")
    public void validateAuthenticatorExists(String name) {
        log.debug("[VALIDATION] Authenticator exists: {}", name);
    }

    @EntityExistence(repository = it.aredegalli.auctoritas.repository.role.RoleRepository.class, idParam = "id")
    public void validateRoleExists(Role role) {
        log.debug("[VALIDATION] Role exists: {}", role.getName());
    }

    @EntityExistence(repository = it.aredegalli.auctoritas.repository.user.UserRepository.class, idParam = "id")
    public void validateUserExists(User user) {
        log.debug("[VALIDATION] User exists: {}", user.getId());
    }
}

