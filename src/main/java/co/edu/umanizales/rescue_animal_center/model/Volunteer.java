package co.edu.umanizales.rescue_animal_center.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class Volunteer extends Person {
    private int hoursPerWeek;

    public Volunteer(String id, String firstName, String lastName, String idNumber, String phone, Address address, int hoursPerWeek) {
        super(id, firstName, lastName, idNumber, phone, address);
        this.hoursPerWeek = hoursPerWeek;
    }
}
