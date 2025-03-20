package it.aredegalli.auctoritas.model.role;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "permissions_role",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"role_id", "permission_id"})})
public class PermissionRole {
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne(optional = false)
    @JoinColumn(name = "permission_id")
    private Permission permission;
}