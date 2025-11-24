package co.edu.umanizales.rescue_animal_center.model.dto;

import co.edu.umanizales.rescue_animal_center.model.AnimalStatus;
import co.edu.umanizales.rescue_animal_center.model.Gender;
import co.edu.umanizales.rescue_animal_center.model.Species;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AnimalRequest {
    @NotBlank
    private String name;

    @NotNull
    private Species species;

    @Min(0)
    private int age;

    @NotNull
    private Gender gender;

    @NotNull
    private AnimalStatus status;

    @NotBlank
    private String habitatId;
}
