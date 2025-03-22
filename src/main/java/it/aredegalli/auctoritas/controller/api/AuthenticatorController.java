package it.aredegalli.auctoritas.controller.api;

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

    @GetMapping("/active/{id}")
    public ResponseEntity<Boolean> isAuthenticatorActive(@PathVariable @NotNull UUID id) {
        log.info("[API] isAuthenticatorActive: {}", id);
        return ResponseEntity.ok(authenticatorService.isAuthenticatorActive(id));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<AuthenticatorDto> getAuthenticatorByName(@PathVariable String name) {
        log.info("[API] getAuthenticatorByName: {}", name);
        return ResponseEntity.ok(authenticatorService.getAuthenticatorByName(name));
    }

    @PostMapping
    public ResponseEntity<UUID> createAuthenticator(@Valid @RequestBody AuthenticatorSaveDto dto) {
        log.info("[API] createAuthenticator: {}", dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(authenticatorService.createAuthenticator(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UUID> updateAuthenticator(@PathVariable UUID id, @Valid @RequestBody AuthenticatorSaveDto dto) {
        log.info("[API] updateAuthenticator {} with dto: {}", id, dto);
        return ResponseEntity.ok(authenticatorService.updateAuthenticator(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthenticator(@PathVariable UUID id) {
        log.info("[API] deleteAuthenticator: {}", id);
        authenticatorService.deleteAuthenticator(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/mapping/user/{userId}")
    public ResponseEntity<List<UserAuthMappingDto>> getMappingsByUser(@PathVariable UUID userId) {
        log.info("[API] getMappingsByUser: {}", userId);
        return ResponseEntity.ok(authenticatorService.getMappingsByUserId(userId));
    }

    @PostMapping("/mapping")
    public ResponseEntity<UUID> createMapping(@RequestParam UUID userId,
                                              @RequestParam UUID authenticatorId,
                                              @RequestParam String externalUserId) {
        log.info("[API] createMapping: userId={}, authenticatorId={}, externalUserId={}", userId, authenticatorId, externalUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                authenticatorService.createMapping(userId, authenticatorId, externalUserId)
        );
    }

    @DeleteMapping("/mapping/{mappingId}")
    public ResponseEntity<Void> deleteMapping(@PathVariable UUID mappingId) {
        log.info("[API] deleteMapping: {}", mappingId);
        authenticatorService.deleteMapping(mappingId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/app/active/{id}")
    public ResponseEntity<Boolean> isAppAuthenticatorActive(@PathVariable UUID id) {
        log.info("[API] isAppAuthenticatorActive: {}", id);
        return ResponseEntity.ok(authenticatorService.isAppAuthenticatorActive(id));
    }

    @GetMapping("/app/{applicationId}")
    public ResponseEntity<List<ApplicationAuthenticatorDto>> getAppAuthenticators(@PathVariable UUID applicationId) {
        log.info("[API] getAppAuthenticators: {}", applicationId);
        return ResponseEntity.ok(authenticatorService.getAppAuthenticators(applicationId));
    }

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

    @DeleteMapping("/app/{id}")
    public ResponseEntity<Void> deleteAppAuthenticator(@PathVariable UUID id) {
        log.info("[API] deleteAppAuthenticator: {}", id);
        authenticatorService.deleteAppAuthenticator(id);
        return ResponseEntity.noContent().build();
    }
}
