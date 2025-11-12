package co.edu.umanizales.rescue_animal_center.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Habitat {
    private String id;
    private String name;
    private String description;
}
