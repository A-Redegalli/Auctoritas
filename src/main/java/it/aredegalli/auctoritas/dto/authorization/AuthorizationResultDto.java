package it.aredegalli.auctoritas.dto.authorization;

import lombok.Builder;
import lombok.Data;

import java.util.Map;
import java.util.UUID;

@Data
@Builder
public class AuthorizationResultDto {
    private UUID userId;
    private Map<UUID, String> roles;
}
