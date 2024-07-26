package br.dev.drufontael.Trip_Planer.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto {
    private Long id;
    @NotNull(message = "Description cannot be null")
    @Size(min = 1, max = 255, message = "Description must be between 1 and 255 characters")
    private String description;
    @NotNull(message = "Date cannot be null")
    private LocalDate date;
}
