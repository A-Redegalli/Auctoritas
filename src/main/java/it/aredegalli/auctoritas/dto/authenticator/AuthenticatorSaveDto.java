package it.aredegalli.auctoritas.dto.authenticator;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticatorSaveDto {

    @NotNull(message = "Name is required")
    private String name;

    @NotNull(message = "Auth type is required")
    private String authType;

    @NotNull(message = "Config is required")
    private String config;

}
