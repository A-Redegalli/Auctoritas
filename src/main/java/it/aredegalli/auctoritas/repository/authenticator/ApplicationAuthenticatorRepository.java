package it.aredegalli.auctoritas.repository.authenticator;

import it.aredegalli.auctoritas.model.authenticator.ApplicationAuthenticator;
import it.aredegalli.auctoritas.repository.UUIDRepository;

import java.util.List;
import java.util.UUID;

public interface ApplicationAuthenticatorRepository extends UUIDRepository<ApplicationAuthenticator> {
    List<ApplicationAuthenticator> findByApplicationIdOrderByDisplayOrder(UUID applicationId);
}