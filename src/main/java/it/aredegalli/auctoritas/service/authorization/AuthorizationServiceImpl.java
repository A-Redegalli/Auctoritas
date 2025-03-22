package it.aredegalli.auctoritas.service.authorization;

import it.aredegalli.auctoritas.dto.authorization.AuthorizationResultDto;
import it.aredegalli.auctoritas.exception.NotFoundException;
import it.aredegalli.auctoritas.model.application.Application;
import it.aredegalli.auctoritas.model.application.UserRoleApplication;
import it.aredegalli.auctoritas.model.authenticator.ApplicationAuthenticator;
import it.aredegalli.auctoritas.model.authenticator.Authenticator;
import it.aredegalli.auctoritas.model.authenticator.UserAuthMapping;
import it.aredegalli.auctoritas.model.role.Role;
import it.aredegalli.auctoritas.model.user.User;
import it.aredegalli.auctoritas.repository.application.ApplicationRepository;
import it.aredegalli.auctoritas.repository.application.UserRoleApplicationRepository;
import it.aredegalli.auctoritas.repository.authenticator.ApplicationAuthenticatorRepository;
import it.aredegalli.auctoritas.repository.authenticator.AuthenticatorRepository;
import it.aredegalli.auctoritas.repository.authenticator.UserAuthMappingRepository;
import it.aredegalli.auctoritas.repository.user.UserRepository;
import it.aredegalli.auctoritas.service.audit.AuditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorizationServiceImpl implements AuthorizationService {
    private final AuthenticatorRepository authenticatorRepository;
    private final ApplicationRepository applicationRepository;
    private final ApplicationAuthenticatorRepository appAuthRepository;
    private final UserAuthMappingRepository userAuthMappingRepository;
    private final UserRepository userRepository;
    private final UserRoleApplicationRepository userRoleApplicationRepository;
    private final AuditService auditService;
    private final AuthorizationHelper authorizationHelper;

    @Override
    @Transactional
    public AuthorizationResultDto authorizeAccess(String applicationName, String authenticatorName, String externalUserId) {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("applicationName", applicationName);
        metadata.put("authenticatorName", authenticatorName);
        metadata.put("externalUserId", externalUserId);

        Authenticator authenticator = getActiveAuthenticator(authenticatorName, metadata);
        Application application = getApplication(applicationName, metadata);
        validateApplicationAuthenticator(application, authenticator);
        User user = getOrCreateUser(authenticator, externalUserId, applicationName, metadata);
        metadata.put("userId", user.getId());

        List<Role> roles = getUserRolesOrAssignDefault(user, application, applicationName, metadata);
        Map<UUID, String> roleMap = roles.stream().collect(Collectors.toMap(Role::getId, Role::getName));
        metadata.put("roles", roleMap);

        log.info("[AUTH] Authorized user {} with roles {} on app {}", user.getId(), roleMap.values(), applicationName);
        authorizationHelper.logAccessGranted(user, applicationName, "Access granted with assigned roles.", metadata);

        return AuthorizationResultDto.builder()
                .userId(user.getId())
                .roles(roleMap)
                .build();
    }

    private Authenticator getActiveAuthenticator(String name, Map<String, Object> metadata) {
        authorizationHelper.validateAuthenticatorExists(name);
        return authenticatorRepository.findByName(name)
                .filter(Authenticator::isActive)
                .orElseThrow(() -> new NotFoundException("Authenticator not found or inactive"));
    }

    private Application getApplication(String name, Map<String, Object> metadata) {
        authorizationHelper.validateApplicationExists(name);
        return applicationRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("Application not found"));
    }

    private void validateApplicationAuthenticator(Application application, Authenticator authenticator) {
        appAuthRepository.findByApplicationIdAndAuthenticatorId(application.getId(), authenticator.getId())
                .filter(ApplicationAuthenticator::isActive)
                .orElseThrow(() -> new NotFoundException("Auth provider not active for this application"));
    }

    private User getOrCreateUser(Authenticator authenticator, String externalUserId, String appName, Map<String, Object> metadata) {
        return userAuthMappingRepository.findByAuthenticatorIdAndExternalUserId(authenticator.getId(), externalUserId)
                .map(UserAuthMapping::getUser)
                .orElseGet(() -> {
                    User user = userRepository.save(User.builder().build());
                    UserAuthMapping mapping = UserAuthMapping.builder()
                            .user(user)
                            .authenticator(authenticator)
                            .externalUserId(externalUserId)
                            .build();
                    userAuthMappingRepository.save(mapping);
                    log.info("[AUTH] Created new user {} and mapping for externalId {}", user.getId(), externalUserId);
                    authorizationHelper.logAccessGranted(user, appName, "User created and mapped on login.", metadata);
                    return user;
                });
    }

    private List<Role> getUserRolesOrAssignDefault(User user, Application application, String appName, Map<String, Object> metadata) {
        List<UserRoleApplication> uraList = userRoleApplicationRepository.findAllByUserIdAndApplicationId(user.getId(), application.getId());

        if (!uraList.isEmpty()) {
            return uraList.stream().map(UserRoleApplication::getRole).collect(Collectors.toList());
        } else if (application.getDefaultRole() != null) {
            Role defaultRole = application.getDefaultRole();
            UserRoleApplication newURA = UserRoleApplication.builder()
                    .user(user)
                    .application(application)
                    .role(defaultRole)
                    .build();
            userRoleApplicationRepository.save(newURA);
            log.info("[AUTH] Assigned default role {} to user {} for application {}", defaultRole.getName(), user.getId(), appName);
            authorizationHelper.logAccessGranted(user, appName, "Assigned default role to user on first login.", metadata);
            return List.of(defaultRole);
        } else {
            authorizationHelper.logAccessDenied(user, appName, "Access denied: no assigned role and no default role.", metadata);
            throw new NotFoundException("Access denied: no assigned role.");
        }
    }
}
