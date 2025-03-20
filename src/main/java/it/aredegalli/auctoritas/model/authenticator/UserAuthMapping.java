package it.aredegalli.auctoritas.model.authenticator;

import it.aredegalli.auctoritas.model.user.User;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "user_auth_mappings",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"authenticator_id", "external_user_id"})})
public class UserAuthMapping {
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "authenticator_id")
    private Authenticator authenticator;

    @Column(nullable = false)
    private String externalUserId;
}