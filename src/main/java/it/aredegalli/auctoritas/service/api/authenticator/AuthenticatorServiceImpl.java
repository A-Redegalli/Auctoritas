package it.aredegalli.auctoritas.service.api.authenticator;

import it.aredegalli.auctoritas.dto.authenticator.ApplicationAuthenticatorDto;
import it.aredegalli.auctoritas.dto.authenticator.AuthenticatorDto;
import it.aredegalli.auctoritas.dto.authenticator.AuthenticatorSaveDto;
import it.aredegalli.auctoritas.dto.authenticator.UserAuthMappingDto;
import it.aredegalli.auctoritas.enums.AuditEventTypeEnum;
import it.aredegalli.auctoritas.exception.ConflictException;
import it.aredegalli.auctoritas.model.application.Application;
import it.aredegalli.auctoritas.model.authenticator.ApplicationAuthenticator;
import it.aredegalli.auctoritas.model.authenticator.Authenticator;
import it.aredegalli.auctoritas.model.authenticator.UserAuthMapping;
import it.aredegalli.auctoritas.model.user.User;
import it.aredegalli.auctoritas.repository.application.ApplicationRepository;
import it.aredegalli.auctoritas.repository.authenticator.ApplicationAuthenticatorRepository;
import it.aredegalli.auctoritas.repository.authenticator.AuthenticatorRepository;
import it.aredegalli.auctoritas.repository.authenticator.UserAuthMappingRepository;
import it.aredegalli.auctoritas.repository.user.UserRepository;
import it.aredegalli.auctoritas.security.encryption.EncryptionService;
import it.aredegalli.auctoritas.service.audit.annotation.Audit;
import it.aredegalli.auctoritas.service.validation.annotation.EntityExistence;
import it.aredegalli.auctoritas.util.HashUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticatorServiceImpl implements AuthenticatorService {

    private final AuthenticatorRepository authenticatorRepository;
    private final ApplicationAuthenticatorRepository applicationAuthenticatorRepository;
    private final UserAuthMappingRepository userAuthMappingRepository;
    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final EncryptionService encryptionService;
    private final HashUtil hashUtil;

    @Override
    @EntityExistence(repository = AuthenticatorRepository.class, idParam = "authenticatorId")
    @Audit(event = AuditEventTypeEnum.AUTHENTICATOR_ACTIVE, description = "Authenticator active status check")
    public boolean isAuthenticatorActive(UUID authenticatorId) {
        boolean active = authenticatorRepository.findById(authenticatorId)
                .map(Authenticator::isActive)
                .orElse(false);
        log.info("[API] Authenticator {} active status: {}", authenticatorId, active);
        return active;
    }

    @Override
    @EntityExistence(repository = AuthenticatorRepository.class, idParam = "name", method = "findByName")
    @Audit(event = AuditEventTypeEnum.AUTHENTICATOR_GET, description = "Get authenticator by name")
    public AuthenticatorDto getAuthenticatorByName(String name) {
        Authenticator authenticator = authenticatorRepository.findByName(name).orElse(null);
        assert authenticator != null;
        AuthenticatorDto dto = AuthenticatorDto.builder().id(authenticator.getId())
                .name(authenticator.getName())
                .authType(authenticator.getAuthType())
                .config(this.encryptionService.decrypt(authenticator.getConfig()))
                .isActive(authenticator.isActive())
                .build();
        log.info("[API] Retrieved authenticator by name '{}': {}", name, dto);
        return dto;
    }

    @Override
    @Audit(event = AuditEventTypeEnum.AUTHENTICATOR_GET_ALL, description = "Get all authenticators")
    public List<AuthenticatorDto> getAllAuthenticators() {
        return this.authenticatorRepository.findAll()
                .stream()
                .map(auth -> AuthenticatorDto.builder()
                        .id(auth.getId())
                        .name(auth.getName())
                        .authType(auth.getAuthType())
                        .config(this.encryptionService.decrypt(auth.getConfig()))
                        .isActive(auth.isActive())
                        .build())
                .toList();
    }

    @Override
    @Audit(event = AuditEventTypeEnum.AUTHENTICATOR_CREATE, description = "Create authenticator")
    public UUID createAuthenticator(AuthenticatorSaveDto saveDto) {
        String encryptedConfig = this.encryptionService.encrypt(saveDto.getConfig());

        Authenticator authenticator = Authenticator.builder()
                .name(saveDto.getName())
                .authType(saveDto.getAuthType())
                .config(encryptedConfig)
                .build();
        authenticator = authenticatorRepository.save(authenticator);
        log.info("[API] Created authenticator {} with dto: {}", authenticator.getId(), saveDto);
        return authenticator.getId();
    }

    @Override
    @EntityExistence(repository = AuthenticatorRepository.class, idParam = "id")
    @Audit(event = AuditEventTypeEnum.AUTHENTICATOR_UPDATE, description = "Update authenticator")
    public UUID updateAuthenticator(UUID id, AuthenticatorSaveDto saveDto) {
        String encryptedConfig = this.encryptionService.encrypt(saveDto.getConfig());
        Authenticator authenticator = authenticatorRepository.findById(id).orElse(null);
        assert authenticator != null;
        authenticator.setName(saveDto.getName());
        authenticator.setAuthType(saveDto.getAuthType());
        authenticator.setConfig(encryptedConfig);
        authenticatorRepository.save(authenticator);
        log.info("[API] Updated authenticator {} with dto: {}", id, saveDto);
        return id;
    }

    @Override
    @EntityExistence(repository = AuthenticatorRepository.class, idParam = "id")
    @Audit(event = AuditEventTypeEnum.AUTHENTICATOR_DELETE, description = "Delete authenticator")
    public void deleteAuthenticator(UUID id) {
        authenticatorRepository.deleteById(id);
        log.info("[API] Deleted authenticator {}", id);
    }

    @Override
    @EntityExistence(repository = UserRepository.class, idParam = "userId")
    @Audit(event = AuditEventTypeEnum.USER_AUTHENTICATOR_MAPPING_GET, description = "Get mappings by user")
    public List<UserAuthMappingDto> getMappingsByUserId(UUID userId) {
        List<UserAuthMappingDto> mappings = userAuthMappingRepository.findByUserId(userId).stream()
                .map(UserAuthMappingDto::new)
                .toList();
        log.info("[API] Retrieved {} mappings for user {}", mappings.size(), userId);
        return mappings;
    }

    @Override
    @Audit(event = AuditEventTypeEnum.USER_AUTHENTICATOR_MAPPING_CREATE, description = "Create user auth mapping")
    public UUID createMapping(UUID userId, UUID authenticatorId, String externalUserId) {
        if (userAuthMappingRepository.existsByAuthenticatorIdAndExternalUserId(authenticatorId, externalUserId)) {
            log.warn("[API] Conflict: User with externalId {} already mapped for authenticator {}", externalUserId, authenticatorId);
            throw new ConflictException("User already mapped for this authenticator");
        }
        User user = userRepository.findById(userId).orElse(null);
        Authenticator authenticator = authenticatorRepository.findById(authenticatorId).orElse(null);
        assert user != null && authenticator != null;
        UserAuthMapping mapping = UserAuthMapping.builder()
                .user(user)
                .authenticator(authenticator)
                .externalUserId(hashUtil.hmacSha256(externalUserId))
                .build();
        mapping = userAuthMappingRepository.save(mapping);
        log.info("[API] Created auth mapping {} for user {}", mapping.getId(), userId);
        return mapping.getId();
    }

    @Override
    @Audit(event = AuditEventTypeEnum.USER_AUTHENTICATOR_MAPPING_DELETE, description = "Delete user auth mapping")
    public void deleteMapping(UUID mappingId) {
        userAuthMappingRepository.deleteById(mappingId);
        log.info("[API] Deleted auth mapping {}", mappingId);
    }

    @Override
    @EntityExistence(repository = ApplicationAuthenticatorRepository.class, idParam = "id")
    @Audit(event = AuditEventTypeEnum.APPLICATION_AUTHENTICATOR_ACTIVE, description = "Check app authenticator active")
    public boolean isAppAuthenticatorActive(UUID id) {
        boolean active = applicationAuthenticatorRepository.findById(id)
                .map(ApplicationAuthenticator::isActive)
                .orElse(false);
        log.info("[API] AppAuthenticator {} active status: {}", id, active);
        return active;
    }

    @Override
    @EntityExistence(repository = ApplicationRepository.class, idParam = "applicationId")
    @Audit(event = AuditEventTypeEnum.APPLICATION_AUTHENTICATOR_GET, description = "Get app authenticators")
    public List<ApplicationAuthenticatorDto> getAppAuthenticators(UUID applicationId) {
        List<ApplicationAuthenticatorDto> list = applicationAuthenticatorRepository.findByApplicationId(applicationId)
                .stream()
                .map(auth -> ApplicationAuthenticatorDto.builder()
                        .id(auth.getId())
                        .authenticatorId(auth.getAuthenticator().getId())
                        .config(this.encryptionService.decrypt(auth.getConfig()))
                        .displayOrder(auth.getDisplayOrder())
                        .isActive(auth.isActive())
                        .build())
                .toList();
        log.info("[API] Retrieved {} authenticators for app {}", list.size(), applicationId);
        return list;
    }

    @Override
    @Audit(event = AuditEventTypeEnum.APPLICATION_AUTHENTICATOR_CREATE, description = "Create app authenticator")
    public UUID createAppAuthenticator(UUID applicationId, UUID authenticatorId, String config, int displayOrder) {
        if (applicationAuthenticatorRepository.existsByApplicationIdAndAuthenticatorId(applicationId, authenticatorId)) {
            log.warn("[API] Conflict: Authenticator {} already assigned to application {}", authenticatorId, applicationId);
            throw new ConflictException("Authenticator already assigned to this application");
        }
        Application app = applicationRepository.findById(applicationId).orElse(null);
        Authenticator auth = authenticatorRepository.findById(authenticatorId).orElse(null);
        assert app != null && auth != null;
        ApplicationAuthenticator appAuth = ApplicationAuthenticator.builder()
                .application(app)
                .authenticator(auth)
                .config(this.encryptionService.encrypt(config))
                .displayOrder(displayOrder)
                .isActive(true)
                .build();
        appAuth = applicationAuthenticatorRepository.save(appAuth);
        log.info("[API] Created app authenticator {} for app {}", appAuth.getId(), applicationId);
        return appAuth.getId();
    }

    @Override
    @EntityExistence(repository = ApplicationAuthenticatorRepository.class, idParam = "id")
    @Audit(event = AuditEventTypeEnum.APPLICATION_AUTHENTICATOR_DELETE, description = "Delete app authenticator")
    public void deleteAppAuthenticator(UUID id) {
        applicationAuthenticatorRepository.deleteById(id);
        log.info("[API] Deleted app authenticator {}", id);
    }
}
