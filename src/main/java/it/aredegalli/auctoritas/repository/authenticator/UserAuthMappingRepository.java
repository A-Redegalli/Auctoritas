package it.aredegalli.auctoritas.repository.authenticator;

import it.aredegalli.auctoritas.model.authenticator.UserAuthMapping;
import it.aredegalli.auctoritas.repository.UUIDRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserAuthMappingRepository extends UUIDRepository<UserAuthMapping> {
    Optional<UserAuthMapping> findByAuthenticatorIdAndExternalUserId(UUID authenticatorId, String externalUserId);
}