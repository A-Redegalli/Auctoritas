package it.aredegalli.auctoritas.controller.api;

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

    @GetMapping()
    public ResponseEntity<ApplicationDto> getApplicationByName(@RequestParam("name") @NotNull String name) {
        log.info("[API] getApplicationByName: {}", name);
        return ResponseEntity.status(HttpStatus.OK).body(applicationService.getApplicationByName(name));
    }

    @PostMapping()
    public ResponseEntity<UUID> createApplication(@Valid @RequestBody ApplicationSaveDto saveDto) {
        log.info("[API] createApplication with dto: {}", saveDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(applicationService.createApplication(saveDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UUID> updateApplication(@PathVariable @NotNull UUID id, @Valid @RequestBody ApplicationSaveDto saveDto) {
        log.info("[API] updateApplication with id {} with dto: {}", id, saveDto);
        return ResponseEntity.status(HttpStatus.OK).body(applicationService.updateApplication(id, saveDto));
    }

}
