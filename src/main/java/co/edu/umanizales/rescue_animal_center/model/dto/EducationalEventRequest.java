package co.edu.umanizales.rescue_animal_center.model.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EducationalEventRequest {
    @NotBlank
    private String title;
    @NotNull
    @FutureOrPresent
    private LocalDate date;
    @NotBlank
    private String location;
}
