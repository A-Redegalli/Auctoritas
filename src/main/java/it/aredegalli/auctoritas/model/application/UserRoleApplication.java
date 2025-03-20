package it.aredegalli.auctoritas.model.application;

import it.aredegalli.auctoritas.model.role.Role;
import it.aredegalli.auctoritas.model.user.User;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "users_roles_application",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "application_id", "role_id"})})
public class UserRoleApplication {
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "application_id")
    private Application application;

    @ManyToOne(optional = false)
    @JoinColumn(name = "role_id")
    private Role role;
}