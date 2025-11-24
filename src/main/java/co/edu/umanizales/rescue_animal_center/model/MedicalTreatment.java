package co.edu.umanizales.rescue_animal_center.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicalTreatment {
    private String id;
    private String animalId;
    private TreatmentType type;
    private LocalDate startDate;
    private LocalDate endDate;
    private String notes;
}
