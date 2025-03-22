package it.aredegalli.auctoritas.service.validation;

import it.aredegalli.auctoritas.repository.application.ApplicationRepository;
import it.aredegalli.auctoritas.service.validation.annotation.EntityExistence;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EntityValidationHelper {

    @EntityExistence(repository = ApplicationRepository.class, idParam = "name", method = "existsByName")
    public void checkApplicationExistsByName(String name) {
        log.debug("[API] Application {} exists", name);
    }

    @EntityExistence(repository = ApplicationRepository.class, idParam = "name", method = "existsByName", check = false)
    public void checkApplicationNotExistsByName(String name) {
        log.debug("[API] Application {} not exists", name);
    }

}
