package co.edu.umanizales.rescue_animal_center.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Person {
    private String id;
    private String firstName;
    private String lastName;
    private String idNumber;
    private String phone;
    private Address address;
}
