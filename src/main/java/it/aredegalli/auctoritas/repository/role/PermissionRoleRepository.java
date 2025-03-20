package it.aredegalli.auctoritas.repository.role;

import it.aredegalli.auctoritas.model.role.PermissionRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PermissionRoleRepository extends JpaRepository<PermissionRole, UUID> {
    List<PermissionRole> findByRoleId(UUID roleId);
}