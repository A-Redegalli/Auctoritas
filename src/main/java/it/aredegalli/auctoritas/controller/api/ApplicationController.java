package it.aredegalli.auctoritas.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.aredegalli.auctoritas.dto.application.ApplicationDto;
import it.aredegalli.auctoritas.dto.application.ApplicationSaveDto;
import it.aredegalli.auctoritas.service.api.application.ApplicationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Validated
@Slf4j
@RestController
@RequestMapping("/api/application")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @Operation(summary = "Get Application by Name", description = "Retrieve application details by its unique name.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Application found"),
            @ApiResponse(responseCode = "404", description = "Application not found")
    })
    @GetMapping()
    public ResponseEntity<ApplicationDto> getApplicationByName(
            @Parameter(description = "Unique name of the application")
            @RequestParam("name") @NotNull String name) {
        log.info("[API] getApplicationByName: {}", name);
        return ResponseEntity.status(HttpStatus.OK).body(applicationService.getApplicationByName(name));
    }

    @Operation(summary = "Get All Applications", description = "Retrieve all applications in the system.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Applications found"),
            @ApiResponse(responseCode = "404", description = "No applications found")
    })
    @GetMapping("/all")
    public ResponseEntity<Iterable<ApplicationDto>> getAllApplications() {
        log.info("[API] getAllApplications");
        return ResponseEntity.status(HttpStatus.OK).body(applicationService.getAllApplications());
    }

    @Operation(summary = "Create Application", description = "Create a new application entry in the system.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Application created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping()
    public ResponseEntity<UUID> createApplication(
            @Parameter(description = "Data for the new application")
            @Valid @RequestBody ApplicationSaveDto saveDto) {
        log.info("[API] createApplication with dto: {}", saveDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(applicationService.createApplication(saveDto));
    }

    @Operation(summary = "Update Application", description = "Update details of an existing application by its UUID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Application updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Application not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UUID> updateApplication(
            @Parameter(description = "UUID of the application to update")
            @PathVariable @NotNull UUID id,
            @Parameter(description = "Updated data for the application")
            @Valid @RequestBody ApplicationSaveDto saveDto) {
        log.info("[API] updateApplication with id {} with dto: {}", id, saveDto);
        return ResponseEntity.status(HttpStatus.OK).body(applicationService.updateApplication(id, saveDto));
    }
}
