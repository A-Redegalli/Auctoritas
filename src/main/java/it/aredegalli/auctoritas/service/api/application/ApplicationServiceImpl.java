package it.aredegalli.auctoritas.service.api.application;


import it.aredegalli.auctoritas.dto.application.ApplicationDto;
import it.aredegalli.auctoritas.dto.application.ApplicationSaveDto;
import it.aredegalli.auctoritas.enums.AuditEventTypeEnum;
import it.aredegalli.auctoritas.model.application.Application;
import it.aredegalli.auctoritas.repository.application.ApplicationRepository;
import it.aredegalli.auctoritas.service.audit.annotation.Audit;
import it.aredegalli.auctoritas.service.validation.EntityValidationHelper;
import it.aredegalli.auctoritas.service.validation.annotation.EntityExistence;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Slf4j
@RequiredArgsConstructor
@Service
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final EntityValidationHelper entityValidationHelper;

    @Override
    @EntityExistence(repository = ApplicationRepository.class, idParam = "name", method = "existsByName")
    @Audit(event = AuditEventTypeEnum.APPLICATION_GET, description = "Get application by name")
    public ApplicationDto getApplicationByName(String name) {
        return this.applicationRepository.findByName(name)
                .map(ApplicationDto::new)
                .orElse(null);
    }

    @Override
    @Audit(event = AuditEventTypeEnum.APPLICATION_GET_ALL, description = "Get all applications")
    public List<ApplicationDto> getAllApplications() {
        return this.applicationRepository.findAll()
                .stream()
                .map(ApplicationDto::new)
                .toList();
    }

    @Override
    @EntityExistence(repository = ApplicationRepository.class, idParam = "id")
    @Audit(event = AuditEventTypeEnum.APPLICATION_UPDATE, description = "Update Application")
    public UUID updateApplication(UUID id, ApplicationSaveDto saveDto) {
        Application application = this.applicationRepository.findById(id)
                .orElse(null);

        assert application != null;
        application.setName(saveDto.getName());
        application.setDescription(saveDto.getDescription());

        application = this.applicationRepository.save(application);
        log.info("[API] Application {} updated with dto: {}", application.getId(), saveDto);

        return application.getId();
    }

    @Override
    @Audit(event = AuditEventTypeEnum.APPLICATION_CREATE, description = "Create Application")
    public UUID createApplication(ApplicationSaveDto saveDto) {
        this.entityValidationHelper.checkApplicationNotExistsByName(saveDto.getName());
        Application application = Application.builder()
                .name(saveDto.getName())
                .description(saveDto.getDescription())
                .build();

        application = this.applicationRepository.save(application);
        log.info("[API] Application {} created with dto: {}", application.getId(), saveDto);

        return application.getId();
    }

}
