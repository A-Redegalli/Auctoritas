package it.aredegalli.auctoritas.service.authorization;

import it.aredegalli.auctoritas.dto.authorization.AuthorizationResultDto;
import org.springframework.transaction.annotation.Transactional;

public interface AuthorizationService {
    @Transactional
    AuthorizationResultDto authorizeAccess(String applicationName, String authenticatorName, String externalUserId);
}
