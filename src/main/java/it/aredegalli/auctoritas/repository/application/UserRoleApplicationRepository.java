package it.aredegalli.auctoritas.repository.application;

import it.aredegalli.auctoritas.model.application.UserRoleApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserRoleApplicationRepository extends JpaRepository<UserRoleApplication, UUID> {
    List<UserRoleApplication> findByUserIdAndApplicationId(UUID userId, UUID applicationId);
}