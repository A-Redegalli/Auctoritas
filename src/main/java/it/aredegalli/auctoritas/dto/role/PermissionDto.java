package it.aredegalli.auctoritas.dto.role;

import it.aredegalli.auctoritas.model.role.Permission;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PermissionDto {

    private UUID id;
    private String name;
    private String description;

    public PermissionDto(Permission permission) {
        this.id = permission.getId();
        this.name = permission.getName();
        this.description = permission.getDescription();
    }

}
