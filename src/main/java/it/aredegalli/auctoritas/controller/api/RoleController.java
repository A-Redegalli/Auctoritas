package it.aredegalli.auctoritas.controller.api;

import it.aredegalli.auctoritas.dto.role.PermissionDto;
import it.aredegalli.auctoritas.dto.role.RoleDto;
import it.aredegalli.auctoritas.dto.role.RoleSaveDto;
import it.aredegalli.auctoritas.service.api.role.RoleService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Validated
@Slf4j
@RestController
@RequestMapping("/api/role")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping("/{id}")
    public ResponseEntity<RoleDto> getRoleById(@PathVariable @NotNull UUID id) {
        log.info("[API] getRoleById: {}", id);
        return ResponseEntity.ok(roleService.getRoleById(id));
    }

    @PostMapping
    public ResponseEntity<UUID> createRole(@Valid @RequestBody RoleSaveDto dto) {
        log.info("[API] createRole: {}", dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(roleService.createRole(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UUID> updateRole(@PathVariable @NotNull UUID id, @Valid @RequestBody RoleSaveDto dto) {
        log.info("[API] updateRole {} with dto: {}", id, dto);
        return ResponseEntity.ok(roleService.updateRole(id, dto));
    }

    @GetMapping("/{id}/permissions")
    public ResponseEntity<List<PermissionDto>> getPermissionsByRoleId(@PathVariable @NotNull UUID id) {
        log.info("[API] getPermissionsByRoleId: {}", id);
        return ResponseEntity.ok(roleService.getPermissionsByRoleId(id));
    }

    @PostMapping("/{roleId}/permissions/{permissionId}")
    public ResponseEntity<UUID> addPermissionToRole(@PathVariable UUID roleId, @PathVariable UUID permissionId) {
        log.info("[API] addPermissionToRole: roleId={}, permissionId={}", roleId, permissionId);
        return ResponseEntity.status(HttpStatus.CREATED).body(roleService.addPermissionToRole(roleId, permissionId));
    }

    @DeleteMapping("/{roleId}/permissions/{permissionId}")
    public ResponseEntity<UUID> removePermissionFromRole(@PathVariable UUID roleId, @PathVariable UUID permissionId) {
        log.info("[API] removePermissionFromRole: roleId={}, permissionId={}", roleId, permissionId);
        return ResponseEntity.ok(roleService.removePermissionFromRole(roleId, permissionId));
    }

    @GetMapping("/application/{applicationId}")
    public ResponseEntity<List<RoleDto>> getRolesByApplication(@PathVariable UUID applicationId) {
        log.info("[API] getRolesByApplication: {}", applicationId);
        return ResponseEntity.ok(roleService.getRolesByApplicationId(applicationId));
    }

    @PostMapping("/application/{applicationId}/role/{roleId}")
    public ResponseEntity<UUID> addRoleToApplication(@PathVariable UUID roleId, @PathVariable UUID applicationId) {
        log.info("[API] addRoleToApplication: roleId={}, applicationId={}", roleId, applicationId);
        return ResponseEntity.status(HttpStatus.CREATED).body(roleService.addRoleToApplication(roleId, applicationId));
    }

    @DeleteMapping("/application/{applicationId}/role/{roleId}")
    public ResponseEntity<UUID> removeRoleFromApplication(@PathVariable UUID roleId, @PathVariable UUID applicationId) {
        log.info("[API] removeRoleFromApplication: roleId={}, applicationId={}", roleId, applicationId);
        return ResponseEntity.ok(roleService.removeRoleFromApplication(roleId, applicationId));
    }

    @GetMapping("/user/{userId}/application/{applicationId}")
    public ResponseEntity<List<RoleDto>> getRolesByUserAndApplication(@PathVariable UUID userId, @PathVariable UUID applicationId) {
        log.info("[API] getRolesByUserAndApplication: userId={}, applicationId={}", userId, applicationId);
        return ResponseEntity.ok(roleService.getRolesByUserIdAndApplicationId(userId, applicationId));
    }

    @PostMapping("/user/{userId}/application/{applicationId}/role/{roleId}")
    public ResponseEntity<UUID> addRoleToUser(@PathVariable UUID userId, @PathVariable UUID roleId, @PathVariable UUID applicationId) {
        log.info("[API] addRoleToUser: userId={}, roleId={}, applicationId={}", userId, roleId, applicationId);
        return ResponseEntity.status(HttpStatus.CREATED).body(roleService.addRoleToUser(userId, roleId, applicationId));
    }

    @DeleteMapping("/user/{userId}/application/{applicationId}/role/{roleId}")
    public ResponseEntity<UUID> removeRoleFromUser(@PathVariable UUID userId, @PathVariable UUID roleId, @PathVariable UUID applicationId) {
        log.info("[API] removeRoleFromUser: userId={}, roleId={}, applicationId={}", userId, roleId, applicationId);
        return ResponseEntity.ok(roleService.removeRoleFromUser(userId, roleId, applicationId));
    }

    // Optional: get permission by id
    @GetMapping("/permission/{id}")
    public ResponseEntity<PermissionDto> getPermissionById(@PathVariable UUID id) {
        log.info("[API] getPermissionById: {}", id);
        return ResponseEntity.ok(roleService.getPermissionById(id));
    }

    @PostMapping("/permission")
    public ResponseEntity<UUID> createPermission(@Valid @RequestBody PermissionDto dto) {
        log.info("[API] createPermission: {}", dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(roleService.createPermission(dto));
    }

    @PutMapping("/permission/{id}")
    public ResponseEntity<UUID> updatePermission(@PathVariable UUID id, @Valid @RequestBody PermissionDto dto) {
        log.info("[API] updatePermission {} with dto: {}", id, dto);
        return ResponseEntity.ok(roleService.updatePermission(id, dto));
    }
}
