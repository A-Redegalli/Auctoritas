package it.aredegalli.auctoritas.controller.api;

import it.aredegalli.auctoritas.dto.application.ApplicationDto;
import it.aredegalli.auctoritas.dto.application.ApplicationSaveDto;
import it.aredegalli.auctoritas.service.api.application.ApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public ApplicationDto getApplicationByName(@RequestParam("name") String name) {
        log.info("[API] getApplicationByName: {}", name);
        return applicationService.getApplicationByName(name);
    }

    @PostMapping()
    public UUID createApplication(@Valid @RequestBody ApplicationSaveDto saveDto) {
        log.info("[API] createApplication with dto: {}", saveDto);
        return applicationService.createApplication(saveDto);
    }

    @PutMapping("/{id}")
    public UUID updateApplication(@PathVariable UUID id, @Valid @RequestBody ApplicationSaveDto saveDto) {
        log.info("[API] updateApplication with id {} with dto: {}", id, saveDto);
        return applicationService.updateApplication(id, saveDto);
    }

}
