package it.aredegalli.auctoritas.service.api.application;

import it.aredegalli.auctoritas.dto.application.ApplicationDto;
import it.aredegalli.auctoritas.dto.application.ApplicationSaveDto;

import java.util.UUID;

public interface ApplicationService {
    ApplicationDto getApplicationByName(String name);

    UUID updateApplication(UUID id, ApplicationSaveDto saveDto);

    UUID createApplication(ApplicationSaveDto saveDto);
}
