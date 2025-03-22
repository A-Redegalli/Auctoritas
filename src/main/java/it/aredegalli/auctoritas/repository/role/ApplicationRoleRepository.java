package it.aredegalli.auctoritas.repository.role;

import it.aredegalli.auctoritas.model.role.ApplicationRole;
import it.aredegalli.auctoritas.repository.UUIDRepository;

import java.util.List;
import java.util.UUID;

public interface ApplicationRoleRepository extends UUIDRepository<ApplicationRole> {
    List<ApplicationRole> findByApplicationId(UUID applicationId);

    boolean existsByRoleIdAndApplicationId(UUID roleId, UUID applicationId);

    ApplicationRole findByRoleIdAndApplicationId(UUID roleId, UUID applicationId);
}