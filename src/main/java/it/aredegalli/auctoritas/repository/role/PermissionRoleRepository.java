package it.aredegalli.auctoritas.repository.role;

import it.aredegalli.auctoritas.model.role.PermissionRole;
import it.aredegalli.auctoritas.repository.UUIDRepository;

import java.util.List;
import java.util.UUID;

public interface PermissionRoleRepository extends UUIDRepository<PermissionRole> {
    List<PermissionRole> findByRoleId(UUID roleId);

    boolean existsByRoleIdAndPermissionId(UUID roleId, UUID permissionId);

    PermissionRole deleteByRoleIdAndPermissionId(UUID roleId, UUID permissionId);
}