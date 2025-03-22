package it.aredegalli.auctoritas.service.api.role;

import it.aredegalli.auctoritas.dto.role.PermissionDto;
import it.aredegalli.auctoritas.dto.role.RoleDto;
import it.aredegalli.auctoritas.dto.role.RoleSaveDto;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for managing roles and permissions.
 */
public interface RoleService {

    /**
     * Retrieves a role by its ID.
     *
     * @param id the UUID of the role
     * @return the RoleDto object
     */
    RoleDto getRoleById(UUID id);

    /**
     * Retrieves a permission by its ID.
     *
     * @param id the UUID of the permission
     * @return the PermissionDto object
     */
    PermissionDto getPermissionById(UUID id);

    /**
     * Updates a role with the given ID.
     *
     * @param id      the UUID of the role
     * @param roleDto the RoleSaveDto object containing updated role information
     * @return the UUID of the updated role
     */
    UUID updateRole(UUID id, RoleSaveDto roleDto);

    /**
     * Creates a new role.
     *
     * @param roleDto the RoleSaveDto object containing role information
     * @return the UUID of the created role
     */
    UUID createRole(RoleSaveDto roleDto);

    /**
     * Updates a permission with the given ID.
     *
     * @param id            the UUID of the permission
     * @param permissionDto the PermissionDto object containing updated permission information
     * @return the UUID of the updated permission
     */
    UUID updatePermission(UUID id, PermissionDto permissionDto);

    /**
     * Creates a new permission.
     *
     * @param permissionDto the PermissionDto object containing permission information
     * @return the UUID of the created permission
     */
    UUID createPermission(PermissionDto permissionDto);

    /**
     * Retrieves a list of permissions by role ID.
     *
     * @param roleId the UUID of the role
     * @return a list of PermissionDto objects
     */
    List<PermissionDto> getPermissionsByRoleId(UUID roleId);

    /**
     * Adds a permission to a role.
     *
     * @param roleId       the UUID of the role
     * @param permissionId the UUID of the permission
     * @return the UUID of the updated role
     */
    UUID addPermissionToRole(UUID roleId, UUID permissionId);

    /**
     * Removes a permission from a role.
     *
     * @param roleId       the UUID of the role
     * @param permissionId the UUID of the permission
     * @return the UUID of the updated role
     */
    UUID removePermissionFromRole(UUID roleId, UUID permissionId);

    /**
     * Retrieves a list of roles by user ID and application ID.
     *
     * @param userId        the UUID of the user
     * @param applicationId the UUID of the application
     * @return a list of RoleDto objects
     */
    List<RoleDto> getRolesByUserIdAndApplicationId(UUID userId, UUID applicationId);

    /**
     * Adds a role to a user.
     *
     * @param userId        the UUID of the user
     * @param roleId        the UUID of the role
     * @param applicationId the UUID of the application
     * @return the UUID of the updated user
     */
    UUID addRoleToUser(UUID userId, UUID roleId, UUID applicationId);

    /**
     * Removes a role from a user.
     *
     * @param userId        the UUID of the user
     * @param roleId        the UUID of the role
     * @param applicationId the UUID of the application
     * @return the UUID of the updated user
     */
    UUID removeRoleFromUser(UUID userId, UUID roleId, UUID applicationId);

    /**
     * Retrieves a list of roles by application ID.
     *
     * @param applicationId the UUID of the application
     * @return a list of RoleDto objects
     */
    List<RoleDto> getRolesByApplicationId(UUID applicationId);

    /**
     * Adds a role to an application.
     *
     * @param roleId        the UUID of the role
     * @param applicationId the UUID of the application
     * @return the UUID of the updated application
     */
    UUID addRoleToApplication(UUID roleId, UUID applicationId);

    /**
     * Removes a role from an application.
     *
     * @param roleId        the UUID of the role
     * @param applicationId the UUID of the application
     * @return the UUID of the updated application
     */
    UUID removeRoleFromApplication(UUID roleId, UUID applicationId);
}