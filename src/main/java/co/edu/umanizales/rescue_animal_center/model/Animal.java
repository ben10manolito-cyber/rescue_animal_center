package co.edu.umanizales.rescue_animal_center.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Animal {
    private String id;
    private String name;
    private Species species;
    private int age;
    private Gender gender;
    private AnimalStatus status;
    private String habitatId;
}
