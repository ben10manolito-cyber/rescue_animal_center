package co.edu.umanizales.rescue_animal_center.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class Adopter extends Person {
    private boolean hasOtherPets;

    public Adopter(String id, String firstName, String lastName, String idNumber, String phone, Address address, boolean hasOtherPets) {
        super(id, firstName, lastName, idNumber, phone, address);
        this.hasOtherPets = hasOtherPets;
    }
}
