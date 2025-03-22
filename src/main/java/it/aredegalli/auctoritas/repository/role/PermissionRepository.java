package it.aredegalli.auctoritas.repository.role;

import it.aredegalli.auctoritas.model.role.Permission;
import it.aredegalli.auctoritas.repository.UUIDRepository;

import java.util.Optional;

public interface PermissionRepository extends UUIDRepository<Permission> {
    Optional<Permission> findByName(String name);
}