package co.edu.umanizales.rescue_animal_center.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Adoption {
    private String id;
    private String adopterId;
    private String animalId;
    private LocalDate date;
    private String notes;
}
