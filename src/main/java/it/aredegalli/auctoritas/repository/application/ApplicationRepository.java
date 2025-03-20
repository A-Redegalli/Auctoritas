package it.aredegalli.auctoritas.repository.application;

import it.aredegalli.auctoritas.model.application.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ApplicationRepository extends JpaRepository<Application, UUID> {
    Optional<Application> findByName(String name);
}