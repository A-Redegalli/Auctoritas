package it.aredegalli.auctoritas.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import it.aredegalli.auctoritas.dto.authenticator.ApplicationAuthenticatorDto;
import it.aredegalli.auctoritas.dto.authenticator.AuthenticatorDto;
import it.aredegalli.auctoritas.dto.authenticator.AuthenticatorSaveDto;
import it.aredegalli.auctoritas.dto.authenticator.UserAuthMappingDto;
import it.aredegalli.auctoritas.service.api.authenticator.AuthenticatorService;
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
@RequestMapping("/api/authenticator")
@RequiredArgsConstructor
public class AuthenticatorController {

    private final AuthenticatorService authenticatorService;

    @Operation(summary = "Check if authenticator is active")
    @GetMapping("/active/{id}")
    public ResponseEntity<Boolean> isAuthenticatorActive(@PathVariable @NotNull UUID id) {
        log.info("[API] isAuthenticatorActive: {}", id);
        return ResponseEntity.ok(authenticatorService.isAuthenticatorActive(id));
    }

    @Operation(summary = "Get authenticator by name")
    @GetMapping("/name/{name}")
    public ResponseEntity<AuthenticatorDto> getAuthenticatorByName(@PathVariable String name) {
        log.info("[API] getAuthenticatorByName: {}", name);
        return ResponseEntity.ok(authenticatorService.getAuthenticatorByName(name));
    }

    @Operation(summary = "Create a new authenticator")
    @PostMapping
    public ResponseEntity<UUID> createAuthenticator(@Valid @RequestBody AuthenticatorSaveDto dto) {
        log.info("[API] createAuthenticator: {}", dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(authenticatorService.createAuthenticator(dto));
    }

    @Operation(summary = "Update an existing authenticator")
    @PutMapping("/{id}")
    public ResponseEntity<UUID> updateAuthenticator(@PathVariable UUID id, @Valid @RequestBody AuthenticatorSaveDto dto) {
        log.info("[API] updateAuthenticator {} with dto: {}", id, dto);
        return ResponseEntity.ok(authenticatorService.updateAuthenticator(id, dto));
    }

    @Operation(summary = "Delete an authenticator")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthenticator(@PathVariable UUID id) {
        log.info("[API] deleteAuthenticator: {}", id);
        authenticatorService.deleteAuthenticator(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get all mappings for a user")
    @GetMapping("/mapping/user/{userId}")
    public ResponseEntity<List<UserAuthMappingDto>> getMappingsByUser(@PathVariable UUID userId) {
        log.info("[API] getMappingsByUser: {}", userId);
        return ResponseEntity.ok(authenticatorService.getMappingsByUserId(userId));
    }

    @Operation(summary = "Create a user authentication mapping")
    @PostMapping("/mapping")
    public ResponseEntity<UUID> createMapping(@RequestParam UUID userId,
                                              @RequestParam UUID authenticatorId,
                                              @RequestParam String externalUserId) {
        log.info("[API] createMapping: userId={}, authenticatorId={}, externalUserId={}", userId, authenticatorId, externalUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                authenticatorService.createMapping(userId, authenticatorId, externalUserId)
        );
    }

    @Operation(summary = "Delete a user authentication mapping")
    @DeleteMapping("/mapping/{mappingId}")
    public ResponseEntity<Void> deleteMapping(@PathVariable UUID mappingId) {
        log.info("[API] deleteMapping: {}", mappingId);
        authenticatorService.deleteMapping(mappingId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Check if app authenticator is active")
    @GetMapping("/app/active/{id}")
    public ResponseEntity<Boolean> isAppAuthenticatorActive(@PathVariable UUID id) {
        log.info("[API] isAppAuthenticatorActive: {}", id);
        return ResponseEntity.ok(authenticatorService.isAppAuthenticatorActive(id));
    }

    @Operation(summary = "Get all authenticators for an application")
    @GetMapping("/app/{applicationId}")
    public ResponseEntity<List<ApplicationAuthenticatorDto>> getAppAuthenticators(@PathVariable UUID applicationId) {
        log.info("[API] getAppAuthenticators: {}", applicationId);
        return ResponseEntity.ok(authenticatorService.getAppAuthenticators(applicationId));
    }

    @Operation(summary = "Create app authenticator mapping")
    @PostMapping("/app")
    public ResponseEntity<UUID> createAppAuthenticator(@RequestParam UUID applicationId,
                                                       @RequestParam UUID authenticatorId,
                                                       @RequestParam String config,
                                                       @RequestParam(defaultValue = "0") int displayOrder) {
        log.info("[API] createAppAuthenticator: appId={}, authId={}, order={}, config={}", applicationId, authenticatorId, displayOrder, config);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                authenticatorService.createAppAuthenticator(applicationId, authenticatorId, config, displayOrder)
        );
    }

    @Operation(summary = "Delete app authenticator mapping")
    @DeleteMapping("/app/{id}")
    public ResponseEntity<Void> deleteAppAuthenticator(@PathVariable UUID id) {
        log.info("[API] deleteAppAuthenticator: {}", id);
        authenticatorService.deleteAppAuthenticator(id);
        return ResponseEntity.noContent().build();
    }
}
