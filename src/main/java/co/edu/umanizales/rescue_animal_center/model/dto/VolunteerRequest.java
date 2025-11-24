package co.edu.umanizales.rescue_animal_center.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VolunteerRequest {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String idNumber;
    @NotBlank
    private String phone;
    @Valid
    @NotNull
    private AddressRequest address;
    @Min(0)
    private int hoursPerWeek;
}
