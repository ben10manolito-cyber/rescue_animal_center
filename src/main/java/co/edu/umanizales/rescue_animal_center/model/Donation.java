package co.edu.umanizales.rescue_animal_center.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Donation {
    private String id;
    private String donorName;
    private DonationType type;
    private BigDecimal amount;
    private LocalDate date;
}
