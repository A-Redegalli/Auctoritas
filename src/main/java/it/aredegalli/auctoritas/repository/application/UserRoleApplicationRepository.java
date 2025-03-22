package it.aredegalli.auctoritas.repository.application;

import it.aredegalli.auctoritas.model.application.UserRoleApplication;
import it.aredegalli.auctoritas.repository.UUIDRepository;

import java.util.List;
import java.util.UUID;

public interface UserRoleApplicationRepository extends UUIDRepository<UserRoleApplication> {
    List<UserRoleApplication> findAllByUserIdAndApplicationId(UUID userId, UUID applicationId);

    boolean existsByUserIdAndRoleIdAndApplicationId(UUID userId, UUID roleId, UUID applicationId);

    UserRoleApplication findByUserIdAndRoleIdAndApplicationId(UUID userId, UUID roleId, UUID applicationId);
}