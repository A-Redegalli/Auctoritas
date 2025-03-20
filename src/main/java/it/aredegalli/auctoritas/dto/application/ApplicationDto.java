package it.aredegalli.auctoritas.dto.application;

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

}
