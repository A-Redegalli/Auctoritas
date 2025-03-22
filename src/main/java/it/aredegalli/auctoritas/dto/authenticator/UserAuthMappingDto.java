package it.aredegalli.auctoritas.dto.authenticator;

import it.aredegalli.auctoritas.model.authenticator.UserAuthMapping;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAuthMappingDto {
    private UUID id;
    private UUID userId;
    private UUID authenticatorId;
    private String externalUserId;

    public UserAuthMappingDto(UserAuthMapping mapping) {
        this.id = mapping.getId();
        this.userId = mapping.getUser().getId();
        this.authenticatorId = mapping.getAuthenticator().getId();
        this.externalUserId = mapping.getExternalUserId();
    }
}
