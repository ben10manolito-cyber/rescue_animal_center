package co.edu.umanizales.rescue_animal_center.config;

import co.edu.umanizales.rescue_animal_center.infrastructure.csv.CsvFileStorage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CsvConfig {
    @Bean
    public CsvFileStorage csvFileStorage() {
        // Store CSV files under a local folder "data" relative to the app working directory
        return new CsvFileStorage("data");
    }
}
