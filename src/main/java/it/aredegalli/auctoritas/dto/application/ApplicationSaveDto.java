package it.aredegalli.auctoritas.dto.application;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ApplicationSaveDto {

    @NotBlank(message = "Name is mandatory")
    private String name;
    private String description;

}
