package co.edu.umanizales.rescue_animal_center.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AdoptionRequest {
    @NotBlank
    private String adopterId;
    @NotBlank
    private String animalId;
    @NotNull
    private LocalDate date;
    private String notes;
}
