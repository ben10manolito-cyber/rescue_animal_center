package co.edu.umanizales.rescue_animal_center.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class Rescuer extends Person {
    private String zone;

    public Rescuer(String id, String firstName, String lastName, String idNumber, String phone, Address address, String zone) {
        super(id, firstName, lastName, idNumber, phone, address);
        this.zone = zone;
    }
}
