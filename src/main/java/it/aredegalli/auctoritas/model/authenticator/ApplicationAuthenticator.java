package it.aredegalli.auctoritas.model.authenticator;

import it.aredegalli.auctoritas.model.application.Application;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Data
@Entity
@Table(name = "application_authenticators",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"application_id", "authenticator_id"})})
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationAuthenticator {
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "application_id")
    private Application application;

    @ManyToOne(optional = false)
    @JoinColumn(name = "authenticator_id")
    private Authenticator authenticator;

    @Column(nullable = false)
    private int displayOrder = 0;

    @Column(nullable = false)
    private boolean isActive = true;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String config;
}