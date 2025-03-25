package it.aredegalli.auctoritas.service.api.application;

import it.aredegalli.auctoritas.dto.application.ApplicationDto;
import it.aredegalli.auctoritas.dto.application.ApplicationSaveDto;
import it.aredegalli.auctoritas.enums.AuditEventTypeEnum;
import it.aredegalli.auctoritas.service.audit.annotation.Audit;

import java.util.List;
import java.util.UUID;

public interface ApplicationService {
    /**
     * Retrieves an application by its name.
     *
     * @param name the name of the application
     * @return the application details
     */
    ApplicationDto getApplicationByName(String name);

    /**
     * Retrieves all applications.
     *
     * @return a list of all applications
     */
    @Audit(event = AuditEventTypeEnum.APPLICATION_GET_ALL, description = "Get all applications")
    List<ApplicationDto> getAllApplications();

    /**
     * Updates an existing application.
     *
     * @param id      the ID of the application to update
     * @param saveDto the new application data
     * @return the ID of the updated application
     */
    UUID updateApplication(UUID id, ApplicationSaveDto saveDto);

    /**
     * Creates a new application.
     *
     * @param saveDto the application data to save
     * @return the ID of the created application
     */
    UUID createApplication(ApplicationSaveDto saveDto);
}
