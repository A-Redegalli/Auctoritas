package it.aredegalli.auctoritas.dto.authenticator;

import it.aredegalli.auctoritas.model.authenticator.Authenticator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticatorDto {

    private UUID id;
    private String name;
    private String authType;
    private String config;
    private boolean isActive;

    public AuthenticatorDto(Authenticator authenticator) {
        this.id = authenticator.getId();
        this.name = authenticator.getName();
        this.authType = authenticator.getAuthType();
        this.config = authenticator.getConfig();
        this.isActive = authenticator.isActive();
    }

}
