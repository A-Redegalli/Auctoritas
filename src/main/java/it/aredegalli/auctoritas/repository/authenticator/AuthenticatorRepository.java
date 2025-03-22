package it.aredegalli.auctoritas.repository.authenticator;

import it.aredegalli.auctoritas.model.authenticator.Authenticator;
import it.aredegalli.auctoritas.repository.UUIDRepository;

import java.util.Optional;

public interface AuthenticatorRepository extends UUIDRepository<Authenticator> {
    Optional<Authenticator> findByName(String name);
}