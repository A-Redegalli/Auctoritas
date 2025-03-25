package it.aredegalli.auctoritas.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.aredegalli.auctoritas.dto.authorization.AuthorizationResultDto;
import it.aredegalli.auctoritas.service.authorization.AuthorizationService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/authorization")
@RequiredArgsConstructor
public class AuthorizationController {

    private final AuthorizationService authorizationService;

    @Operation(summary = "Authorize Access", description = "Authorize a user for a given application and authenticator.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Authorization successful"),
            @ApiResponse(responseCode = "404", description = "Entity not found or inactive"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @GetMapping
    public ResponseEntity<AuthorizationResultDto> authorizeAccess(
            @Parameter(description = "Unique name of the application")
            @RequestParam @NotBlank String applicationName,

            @Parameter(description = "Unique name of the authenticator")
            @RequestParam @NotBlank String authenticatorName,

            @Parameter(description = "External user ID from the authenticator")
            @RequestParam @NotBlank String externalUserId) {

        log.info("[API] authorizeAccess: application={}, authenticator={}, externalId={}",
                applicationName, authenticatorName, externalUserId);

        AuthorizationResultDto result = authorizationService.authorizeAccess(applicationName, authenticatorName, externalUserId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
