package it.aredegalli.auctoritas.service.api.role;

import it.aredegalli.auctoritas.dto.role.PermissionDto;
import it.aredegalli.auctoritas.dto.role.RoleDto;
import it.aredegalli.auctoritas.dto.role.RoleSaveDto;
import it.aredegalli.auctoritas.enums.AuditEventTypeEnum;
import it.aredegalli.auctoritas.exception.NotFoundException;
import it.aredegalli.auctoritas.model.application.Application;
import it.aredegalli.auctoritas.model.application.UserRoleApplication;
import it.aredegalli.auctoritas.model.role.ApplicationRole;
import it.aredegalli.auctoritas.model.role.Permission;
import it.aredegalli.auctoritas.model.role.PermissionRole;
import it.aredegalli.auctoritas.model.role.Role;
import it.aredegalli.auctoritas.model.user.User;
import it.aredegalli.auctoritas.repository.application.ApplicationRepository;
import it.aredegalli.auctoritas.repository.application.UserRoleApplicationRepository;
import it.aredegalli.auctoritas.repository.role.ApplicationRoleRepository;
import it.aredegalli.auctoritas.repository.role.PermissionRepository;
import it.aredegalli.auctoritas.repository.role.PermissionRoleRepository;
import it.aredegalli.auctoritas.repository.role.RoleRepository;
import it.aredegalli.auctoritas.repository.user.UserRepository;
import it.aredegalli.auctoritas.service.audit.annotation.Audit;
import it.aredegalli.auctoritas.service.validation.annotation.EntityExistence;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final PermissionRoleRepository permissionRoleRepository;
    private final UserRoleApplicationRepository userRoleApplicationRepository;
    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;
    private final ApplicationRoleRepository applicationRoleRepository;

    @Override
    @EntityExistence(repository = RoleRepository.class, idParam = "id")
    @Audit(event = AuditEventTypeEnum.ROLE_GET, description = "Get role by id")
    public RoleDto getRoleById(UUID id) {
        return this.roleRepository.findById(id).map(RoleDto::new).orElse(null);
    }

    @Override
    @EntityExistence(repository = PermissionRepository.class, idParam = "id")
    @Audit(event = AuditEventTypeEnum.PERMISSION_GET, description = "Get permission by id")
    public PermissionDto getPermissionById(UUID id) {
        return this.permissionRepository.findById(id).map(PermissionDto::new).orElse(null);
    }

    @Override
    @EntityExistence(repository = RoleRepository.class, idParam = "id")
    @Audit(event = AuditEventTypeEnum.ROLE_UPDATE, description = "Update Role")
    public UUID updateRole(UUID id, RoleSaveDto roleDto) {
        Role role = this.roleRepository.findById(id).orElse(null);

        assert role != null;
        role.setName(roleDto.getName());
        role.setDescription(roleDto.getDescription());

        this.roleRepository.save(role);
        log.info("[API] Role {} updated with dto: {}", id, roleDto);

        return role.getId();
    }

    @Override
    @Audit(event = AuditEventTypeEnum.ROLE_CREATE, description = "Create Role")
    public UUID createRole(RoleSaveDto roleDto) {
        Role role = Role.builder()
                .name(roleDto.getName())
                .description(roleDto.getDescription())
                .build();

        role = this.roleRepository.save(role);
        log.info("[API] Role {} created with dto: {}", role.getId(), roleDto);

        return role.getId();
    }

    @Override
    @EntityExistence(repository = PermissionRepository.class, idParam = "id")
    @Audit(event = AuditEventTypeEnum.PERMISSION_UPDATE, description = "Update Permission")
    public UUID updatePermission(UUID id, PermissionDto permissionDto) {
        Permission permission = this.permissionRepository.findById(id).orElse(null);

        assert permission != null;
        permission.setName(permissionDto.getName());
        permission.setDescription(permissionDto.getDescription());

        this.permissionRepository.save(permission);
        log.info("[API] Permission {} updated with dto: {}", id, permissionDto);

        return permission.getId();
    }

    @Override
    @Audit(event = AuditEventTypeEnum.PERMISSION_CREATE, description = "Create Permission")
    public UUID createPermission(PermissionDto permissionDto) {
        Permission permission = Permission.builder()
                .name(permissionDto.getName())
                .description(permissionDto.getDescription())
                .build();

        permission = this.permissionRepository.save(permission);
        log.info("[API] Permission {} created with dto: {}", permission.getId(), permissionDto);

        return permission.getId();
    }

    @Override
    @EntityExistence(repository = RoleRepository.class, idParam = "roleId")
    @Audit(event = AuditEventTypeEnum.PERMISSION_ROLE_GET, description = "Get permissions by role")
    public List<PermissionDto> getPermissionsByRoleId(UUID roleId) {
        return this.permissionRoleRepository.findByRoleId(roleId).stream()
                .map(permissionRole -> new PermissionDto(permissionRole.getPermission()))
                .toList();
    }

    @Override
    @EntityExistence(repository = RoleRepository.class, idParam = "roleId")
    @EntityExistence(repository = PermissionRepository.class, idParam = "permissionId")
    @Audit(event = AuditEventTypeEnum.PERMISSION_ROLE_CREATE, description = "Add permission to role")
    public UUID addPermissionToRole(UUID roleId, UUID permissionId) {
        Role role = this.roleRepository.findById(roleId).orElse(null);
        Permission permission = this.permissionRepository.findById(permissionId).orElse(null);

        if (this.permissionRoleRepository.existsByRoleIdAndPermissionId(roleId, permissionId)) {
            log.warn("[API] Role {} already have Permission {}", roleId, permissionId);
            throw new NotFoundException("Role already have Permission");
        }

        UUID returnId = this.permissionRoleRepository.save(PermissionRole.builder()
                .role(role)
                .permission(permission)
                .build()).getId();
        log.info("[API] Permission {} added to Role {}", permissionId, roleId);

        return returnId;
    }

    @Override
    @EntityExistence(repository = RoleRepository.class, idParam = "roleId")
    @EntityExistence(repository = PermissionRepository.class, idParam = "permissionId")
    @Audit(event = AuditEventTypeEnum.PERMISSION_ROLE_DELETE, description = "Remove permission from role")
    public UUID removePermissionFromRole(UUID roleId, UUID permissionId) {
        Role role = this.roleRepository.findById(roleId).orElse(null);

        if (!this.permissionRoleRepository.existsByRoleIdAndPermissionId(roleId, permissionId)) {
            log.warn("[API] Role {} does not have Permission {}", roleId, permissionId);
            throw new NotFoundException("Role does not have Permission");
        }

        assert role != null;
        this.permissionRoleRepository.deleteByRoleIdAndPermissionId(roleId, permissionId);
        log.info("[API] Permission {} removed from Role {}", permissionId, roleId);

        return role.getId();
    }

    @Override
    @EntityExistence(repository = UserRepository.class, idParam = "userId")
    @EntityExistence(repository = ApplicationRepository.class, idParam = "applicationId")
    @Audit(event = AuditEventTypeEnum.USER_ROLE_GET, description = "Get roles by user and application")
    public List<RoleDto> getRolesByUserIdAndApplicationId(UUID userId, UUID applicationId) {
        List<RoleDto> roles = this.userRoleApplicationRepository.findByUserIdAndApplicationId(userId, applicationId).stream()
                .map(userRoleApplication -> new RoleDto(userRoleApplication.getRole()))
                .toList();

        log.info("[API] Roles for user {} and application {}: {}", userId, applicationId, roles);

        return roles;
    }

    @Override
    @EntityExistence(repository = UserRepository.class, idParam = "userId")
    @EntityExistence(repository = RoleRepository.class, idParam = "roleId")
    @EntityExistence(repository = ApplicationRepository.class, idParam = "applicationId")
    @Audit(event = AuditEventTypeEnum.USER_ROLE_CREATE, description = "Add role to user")
    public UUID addRoleToUser(UUID userId, UUID roleId, UUID applicationId) {
        User user = this.userRepository.findById(userId).orElse(null);
        Role role = this.roleRepository.findById(roleId).orElse(null);
        Application application = this.applicationRepository.findById(applicationId).orElse(null);
        UserRoleApplication userRoleApplication = this.userRoleApplicationRepository.findByUserIdAndRoleIdAndApplicationId(userId, roleId, applicationId);

        if (userRoleApplication != null) {
            log.warn("[API] User {} already have Role {} in Application {}", userId, roleId, applicationId);
            throw new NotFoundException("User alredy have Role in Application");
        }

        assert user != null && role != null && application != null;
        userRoleApplication = UserRoleApplication.builder()
                .user(user)
                .role(role)
                .application(application)
                .build();

        userRoleApplication = this.userRoleApplicationRepository.save(userRoleApplication);
        log.info("[API] Role {} added to User {} in Application {}", roleId, userId, applicationId);

        return userRoleApplication.getId();
    }

    @Override
    @EntityExistence(repository = UserRepository.class, idParam = "userId")
    @EntityExistence(repository = RoleRepository.class, idParam = "roleId")
    @EntityExistence(repository = ApplicationRepository.class, idParam = "applicationId")
    @Audit(event = AuditEventTypeEnum.USER_ROLE_DELETE, description = "Remove role from user")
    public UUID removeRoleFromUser(UUID userId, UUID roleId, UUID applicationId) {
        UserRoleApplication userRoleApplication = this.userRoleApplicationRepository.findByUserIdAndRoleIdAndApplicationId(userId, roleId, applicationId);

        if (userRoleApplication == null) {
            log.warn("[API] User {} does not have Role {} in Application {}", userId, roleId, applicationId);
            throw new NotFoundException("User does not have Role in Application");
        }

        this.userRoleApplicationRepository.delete(userRoleApplication);
        log.info("[API] Role {} removed from User {} in Application {}", roleId, userId, applicationId);

        return userRoleApplication.getId();
    }

    @Override
    @Audit(event = AuditEventTypeEnum.APPLICATION_ROLE_GET, description = "Get roles by application")
    public List<RoleDto> getRolesByApplicationId(UUID applicationId) {
        List<RoleDto> roles = this.applicationRoleRepository.findByApplicationId(applicationId).stream()
                .map(userRoleApplication -> new RoleDto(userRoleApplication.getRole()))
                .toList();

        log.info("[API] Roles for application {}: {}", applicationId, roles);

        return roles;
    }

    @Override
    @EntityExistence(repository = RoleRepository.class, idParam = "roleId")
    @EntityExistence(repository = ApplicationRepository.class, idParam = "applicationId")
    @Audit(event = AuditEventTypeEnum.APPLICATION_ROLE_CREATE, description = "Add role to application")
    public UUID addRoleToApplication(UUID roleId, UUID applicationId) {
        Role role = this.roleRepository.findById(roleId).orElse(null);
        Application application = this.applicationRepository.findById(applicationId).orElse(null);
        ApplicationRole applicationRole = this.applicationRoleRepository.findByRoleIdAndApplicationId(roleId, applicationId);

        if (applicationRole != null) {
            log.warn("[API] Role {} already exists in Application {}", roleId, applicationId);
            throw new NotFoundException("Role already exists in Application");
        }

        applicationRole = ApplicationRole.builder()
                .role(role)
                .application(application)
                .build();

        applicationRole = this.applicationRoleRepository.save(applicationRole);
        log.info("[API] Role {} added to Application {}", roleId, applicationId);

        return applicationRole.getId();
    }

    @Override
    @EntityExistence(repository = RoleRepository.class, idParam = "roleId")
    @EntityExistence(repository = ApplicationRepository.class, idParam = "applicationId")
    @Audit(event = AuditEventTypeEnum.APPLICATION_ROLE_DELETE, description = "Role Remove From Application")
    public UUID removeRoleFromApplication(UUID roleId, UUID applicationId) {
        ApplicationRole applicationRole = this.applicationRoleRepository.findByRoleIdAndApplicationId(roleId, applicationId);

        if (applicationRole == null) {
            log.warn("[API] Role {} does not exist in Application {}", roleId, applicationId);
            throw new NotFoundException("Role does not exist in Application");
        }

        this.applicationRoleRepository.delete(applicationRole);
        log.info("[API] Role {} removed from Application {}", roleId, applicationId);

        return applicationRole.getId();
    }

}
