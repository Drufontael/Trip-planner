package br.dev.drufontael.Trip_Planer.dto;

import br.dev.drufontael.Trip_Planer.model.enuns.TripCategory;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TripDto {
    private Long id;
    @NotNull(message = "Name cannot be null")
    @Size(min = 1, max = 255, message = "Name must be between 1 and 255 characters")
    private String name;
    @NotNull(message = "Start date cannot be null")
    private LocalDate startDate;
    @NotNull(message = "End date cannot be null")
    private LocalDate endDate;
    @NotNull(message = "Category cannot be null")
    private TripCategory category;
}
