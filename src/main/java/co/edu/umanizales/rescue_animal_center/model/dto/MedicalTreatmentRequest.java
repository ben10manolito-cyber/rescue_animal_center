package co.edu.umanizales.rescue_animal_center.model.dto;

import co.edu.umanizales.rescue_animal_center.model.TreatmentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MedicalTreatmentRequest {
    @NotBlank
    private String animalId;
    @NotNull
    private TreatmentType type;
    @NotNull
    private LocalDate startDate;
    private LocalDate endDate;
    private String notes;
}
