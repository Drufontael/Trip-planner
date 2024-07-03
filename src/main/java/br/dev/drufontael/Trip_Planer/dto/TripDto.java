package br.dev.drufontael.Trip_Planer.dto;

import br.dev.drufontael.Trip_Planer.model.enuns.TripCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TripDto {
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private TripCategory category;
}
