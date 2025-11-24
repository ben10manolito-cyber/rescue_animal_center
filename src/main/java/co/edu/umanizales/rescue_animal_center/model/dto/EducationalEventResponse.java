package co.edu.umanizales.rescue_animal_center.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EducationalEventResponse {
    private String id;
    private String title;
    private LocalDate date;
    private String location;
}
