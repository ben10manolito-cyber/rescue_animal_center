package co.edu.umanizales.rescue_animal_center.model.dto;

import co.edu.umanizales.rescue_animal_center.model.AnimalStatus;
import co.edu.umanizales.rescue_animal_center.model.Gender;
import co.edu.umanizales.rescue_animal_center.model.Species;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnimalResponse {
    private String id;
    private String name;
    private Species species;
    private int age;
    private Gender gender;
    private AnimalStatus status;
    private String habitatId;
}
