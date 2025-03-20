package it.aredegalli.auctoritas.service.api.application;


import it.aredegalli.auctoritas.dto.application.ApplicationDto;
import it.aredegalli.auctoritas.dto.application.ApplicationSaveDto;
import it.aredegalli.auctoritas.enums.AuditEventTypeEnum;
import it.aredegalli.auctoritas.exception.NotFoundException;
import it.aredegalli.auctoritas.model.application.Application;
import it.aredegalli.auctoritas.repository.application.ApplicationRepository;
import it.aredegalli.auctoritas.service.audit.AuditService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;


@Slf4j
@RequiredArgsConstructor
@Service
public class ApplicationServiceImpl implements ApplicationService {

    private final AuditService auditService;
    private final ApplicationRepository applicationRepository;
    private final HttpServletRequest httpServletRequest;

    @Override
    public ApplicationDto getApplicationByName(String name) {
        ApplicationDto applicationDto = this.applicationRepository.findByName(name)
                .map(app -> ApplicationDto.builder()
                        .id(app.getId())
                        .name(app.getName())
                        .description(app.getDescription())
                        .build())
                .orElse(null);

        this.auditService.logEvent(null,
                AuditEventTypeEnum.APPLICATION_GET,
                "Auctoritas",
                "Application Get",
                this.auditService.buildMetadata(
                        AuditEventTypeEnum.APPLICATION_GET,
                        Map.of("applicationName", name,
                                "success", applicationDto != null)
                ));

        if (applicationDto == null) {
            log.warn("[API] Application with name {} not found", name);
            throw new NotFoundException("Application not found");
        }

        return applicationDto;
    }

    @Override
    public UUID updateApplication(UUID id, ApplicationSaveDto saveDto) {
        Application application = this.applicationRepository.findById(id)
                .orElse(null);

        this.auditService.logEvent(null,
                AuditEventTypeEnum.APPLICATION_UPDATE,
                "Auctoritas",
                "Application Update",
                this.auditService.buildMetadata(
                        AuditEventTypeEnum.APPLICATION_GET,
                        Map.of("request-dto", saveDto,
                                "success", application != null)
                ));

        if (application == null) {
            log.warn("[API] Application with name {} not found", saveDto.getName());
            throw new NotFoundException("Application not found");
        }

        application.setName(saveDto.getName());
        application.setDescription(saveDto.getDescription());

        application = this.applicationRepository.save(application);
        log.info("[API] Application {} updated with dto: {}", application.getId(), saveDto);

        return application.getId();
    }

    @Override
    public UUID createApplication(ApplicationSaveDto saveDto) {
        Application application = Application.builder()
                .name(saveDto.getName())
                .description(saveDto.getDescription())
                .build();

        this.auditService.logEvent(null,
                AuditEventTypeEnum.APPLICATION_CREATE,
                "Auctoritas",
                "Application Create",
                this.auditService.buildMetadata(
                        AuditEventTypeEnum.APPLICATION_CREATE,
                        Map.of("request-dto", saveDto)
                ));

        application = this.applicationRepository.save(application);
        log.info("[API] Application {} created with dto: {}", application.getId(), saveDto);

        return application.getId();
    }

}
