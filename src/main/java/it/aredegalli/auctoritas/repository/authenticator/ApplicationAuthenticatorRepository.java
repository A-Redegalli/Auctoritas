package it.aredegalli.auctoritas.repository.authenticator;

import it.aredegalli.auctoritas.model.authenticator.ApplicationAuthenticator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ApplicationAuthenticatorRepository extends JpaRepository<ApplicationAuthenticator, UUID> {
    List<ApplicationAuthenticator> findByApplicationIdOrderByDisplayOrder(UUID applicationId);
}