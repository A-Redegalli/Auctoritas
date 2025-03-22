package it.aredegalli.auctoritas.repository.application;

import it.aredegalli.auctoritas.model.application.Application;
import it.aredegalli.auctoritas.repository.UUIDRepository;

import java.util.Optional;

public interface ApplicationRepository extends UUIDRepository<Application> {
    Optional<Application> findByName(String name);

    boolean existsByName(String name);
}