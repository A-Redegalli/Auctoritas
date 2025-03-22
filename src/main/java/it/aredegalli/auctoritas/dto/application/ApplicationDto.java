package it.aredegalli.auctoritas.dto.application;

import it.aredegalli.auctoritas.model.application.Application;
import lombok.*;

import java.util.UUID;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationDto {

    private UUID id;
    private String name;
    private String description;

    public ApplicationDto(Application application) {
        this.id = application.getId();
        this.name = application.getName();
        this.description = application.getDescription();
    }

}
