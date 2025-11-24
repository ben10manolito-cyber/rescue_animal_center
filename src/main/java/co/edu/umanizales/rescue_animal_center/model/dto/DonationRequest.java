package co.edu.umanizales.rescue_animal_center.model.dto;

import co.edu.umanizales.rescue_animal_center.model.DonationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class DonationRequest {
    @NotBlank
    private String donorName;
    @NotNull
    private DonationType type;
    @NotNull
    @Positive
    private BigDecimal amount;
    @NotNull
    private LocalDate date;
}
