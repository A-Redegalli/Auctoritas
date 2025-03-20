package it.aredegalli.auctoritas.repository.authenticator;

import it.aredegalli.auctoritas.model.authenticator.Authenticator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AuthenticatorRepository extends JpaRepository<Authenticator, UUID> {
    Optional<Authenticator> findByName(String name);
}