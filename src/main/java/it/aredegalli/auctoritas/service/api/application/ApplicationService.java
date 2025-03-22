package it.aredegalli.auctoritas.service.api.application;

import it.aredegalli.auctoritas.dto.application.ApplicationDto;
import it.aredegalli.auctoritas.dto.application.ApplicationSaveDto;

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
