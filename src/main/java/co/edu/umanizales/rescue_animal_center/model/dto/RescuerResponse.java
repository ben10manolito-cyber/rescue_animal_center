package co.edu.umanizales.rescue_animal_center.model.dto;

import co.edu.umanizales.rescue_animal_center.model.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RescuerResponse {
    private String id;
    private String firstName;
    private String lastName;
    private String idNumber;
    private String phone;
    private Address address;
    private String zone;
}
