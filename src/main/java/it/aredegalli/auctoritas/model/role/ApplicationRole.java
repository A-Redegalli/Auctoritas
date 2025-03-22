package it.aredegalli.auctoritas.model.role;

import it.aredegalli.auctoritas.model.application.Application;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@Table(name = "application_roles",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"application_id", "role_id"})})
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationRole {
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "application_id")
    private Application application;

    @ManyToOne(optional = false)
    @JoinColumn(name = "role_id")
    private Role role;
}