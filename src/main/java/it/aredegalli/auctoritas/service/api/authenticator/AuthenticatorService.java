package it.aredegalli.auctoritas.service.api.authenticator;

import it.aredegalli.auctoritas.dto.authenticator.ApplicationAuthenticatorDto;
import it.aredegalli.auctoritas.dto.authenticator.AuthenticatorDto;
import it.aredegalli.auctoritas.dto.authenticator.AuthenticatorSaveDto;
import it.aredegalli.auctoritas.dto.authenticator.UserAuthMappingDto;
import it.aredegalli.auctoritas.enums.AuditEventTypeEnum;
import it.aredegalli.auctoritas.repository.application.ApplicationRepository;
import it.aredegalli.auctoritas.repository.authenticator.ApplicationAuthenticatorRepository;
import it.aredegalli.auctoritas.repository.authenticator.AuthenticatorRepository;
import it.aredegalli.auctoritas.repository.user.UserRepository;
import it.aredegalli.auctoritas.service.audit.annotation.Audit;
import it.aredegalli.auctoritas.service.validation.annotation.EntityExistence;

import java.util.List;
import java.util.UUID;

public interface AuthenticatorService {
    /**
     * Checks if the authenticator is active.
     *
     * @param authenticatorId the UUID of the authenticator
     * @return true if the authenticator is active, false otherwise
     */
    @EntityExistence(repository = AuthenticatorRepository.class, idParam = "authenticatorId")
    @Audit(event = AuditEventTypeEnum.AUTHENTICATOR_ACTIVE, description = "Authenticator activated check")
    boolean isAuthenticatorActive(UUID authenticatorId);

    /**
     * Retrieves an authenticator by its name.
     *
     * @param name the name of the authenticator
     * @return the AuthenticatorDto object
     */
    @EntityExistence(repository = AuthenticatorRepository.class, idParam = "name", method = "findByName")
    @Audit(event = AuditEventTypeEnum.AUTHENTICATOR_GET, description = "Get authenticator by name")
    AuthenticatorDto getAuthenticatorByName(String name);

    /**
     * Retrieves all authenticators.
     *
     * @return a list of AuthenticatorDto objects
     */
    @Audit(event = AuditEventTypeEnum.AUTHENTICATOR_GET_ALL, description = "Get all authenticators")
    List<AuthenticatorDto> getAllAuthenticators();

    /**
     * Creates a new authenticator.
     *
     * @param saveDto the data transfer object containing the authenticator details
     * @return the UUID of the created authenticator
     */
    @Audit(event = AuditEventTypeEnum.AUTHENTICATOR_CREATE, description = "Create authenticator")
    UUID createAuthenticator(AuthenticatorSaveDto saveDto);

    /**
     * Updates an existing authenticator.
     *
     * @param id      the UUID of the authenticator to update
     * @param saveDto the data transfer object containing the updated authenticator details
     * @return the UUID of the updated authenticator
     */
    @EntityExistence(repository = AuthenticatorRepository.class, idParam = "id")
    @Audit(event = AuditEventTypeEnum.AUTHENTICATOR_UPDATE, description = "Update authenticator")
    UUID updateAuthenticator(UUID id, AuthenticatorSaveDto saveDto);

    /**
     * Deletes an authenticator.
     *
     * @param id the UUID of the authenticator to delete
     */
    @EntityExistence(repository = AuthenticatorRepository.class, idParam = "id")
    @Audit(event = AuditEventTypeEnum.AUTHENTICATOR_DELETE, description = "Delete authenticator")
    void deleteAuthenticator(UUID id);

    /**
     * Retrieves user authenticator mappings by user ID.
     *
     * @param userId the UUID of the user
     * @return a list of UserAuthMappingDto objects
     */
    @EntityExistence(repository = UserRepository.class, idParam = "userId")
    @Audit(event = AuditEventTypeEnum.USER_AUTHENTICATOR_MAPPING_GET, description = "Get mappings by user")
    List<UserAuthMappingDto> getMappingsByUserId(UUID userId);

    /**
     * Creates a new user authenticator mapping.
     *
     * @param userId          the UUID of the user
     * @param authenticatorId the UUID of the authenticator
     * @param externalUserId  the external user ID
     * @return the UUID of the created mapping
     */
    @Audit(event = AuditEventTypeEnum.USER_AUTHENTICATOR_MAPPING_CREATE, description = "Create user auth mapping")
    UUID createMapping(UUID userId, UUID authenticatorId, String externalUserId);

    /**
     * Deletes a user authenticator mapping.
     *
     * @param mappingId the UUID of the mapping to delete
     */
    @Audit(event = AuditEventTypeEnum.USER_AUTHENTICATOR_MAPPING_DELETE, description = "Delete user auth mapping")
    void deleteMapping(UUID mappingId);

    /**
     * Checks if the application authenticator is active.
     *
     * @param id the UUID of the application authenticator
     * @return true if the application authenticator is active, false otherwise
     */
    @EntityExistence(repository = ApplicationAuthenticatorRepository.class, idParam = "id")
    @Audit(event = AuditEventTypeEnum.APPLICATION_AUTHENTICATOR_ACTIVE, description = "Check app authenticator active")
    boolean isAppAuthenticatorActive(UUID id);

    /**
     * Retrieves application authenticators by application ID.
     *
     * @param applicationId the UUID of the application
     * @return a list of ApplicationAuthenticatorDto objects
     */
    @EntityExistence(repository = ApplicationRepository.class, idParam = "applicationId")
    @Audit(event = AuditEventTypeEnum.APPLICATION_AUTHENTICATOR_GET, description = "Get app authenticators")
    List<ApplicationAuthenticatorDto> getAppAuthenticators(UUID applicationId);

    /**
     * Creates a new application authenticator.
     *
     * @param applicationId   the UUID of the application
     * @param authenticatorId the UUID of the authenticator
     * @param config          the configuration for the application authenticator
     * @param displayOrder    the display order of the application authenticator
     * @return the UUID of the created application authenticator
     */
    @Audit(event = AuditEventTypeEnum.APPLICATION_AUTHENTICATOR_CREATE, description = "Create app authenticator")
    UUID createAppAuthenticator(UUID applicationId, UUID authenticatorId, String config, int displayOrder);

    /**
     * Deletes an application authenticator.
     *
     * @param id the UUID of the application authenticator to delete
     */
    @EntityExistence(repository = ApplicationAuthenticatorRepository.class, idParam = "id")
    @Audit(event = AuditEventTypeEnum.APPLICATION_AUTHENTICATOR_DELETE, description = "Delete app authenticator")
    void deleteAppAuthenticator(UUID id);
}
