package br.dev.drufontael.Trip_Planer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto {
    private String description;
    private LocalDate date;
}
