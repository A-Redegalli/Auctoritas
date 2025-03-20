package it.aredegalli.auctoritas.repository.role;

import it.aredegalli.auctoritas.model.role.ApplicationRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ApplicationRoleRepository extends JpaRepository<ApplicationRole, UUID> {
    List<ApplicationRole> findByApplicationId(UUID applicationId);
}