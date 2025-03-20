package it.aredegalli.auctoritas.repository.authenticator;

import it.aredegalli.auctoritas.model.authenticator.UserAuthMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserAuthMappingRepository extends JpaRepository<UserAuthMapping, UUID> {
    Optional<UserAuthMapping> findByAuthenticatorIdAndExternalUserId(UUID authenticatorId, String externalUserId);
}