package it.aredegalli.auctoritas.dto.authenticator;

import it.aredegalli.auctoritas.model.authenticator.ApplicationAuthenticator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicationAuthenticatorDto {
    private UUID id;
    private UUID applicationId;
    private UUID authenticatorId;
    private int displayOrder;
    private boolean isActive;
    private String config;

    public ApplicationAuthenticatorDto(ApplicationAuthenticator entity) {
        this.id = entity.getId();
        this.applicationId = entity.getApplication().getId();
        this.authenticatorId = entity.getAuthenticator().getId();
        this.displayOrder = entity.getDisplayOrder();
        this.isActive = entity.isActive();
        this.config = entity.getConfig();
    }
}
