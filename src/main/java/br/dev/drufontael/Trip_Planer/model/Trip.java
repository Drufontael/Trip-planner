package br.dev.drufontael.Trip_Planer.model;

import br.dev.drufontael.Trip_Planer.model.enuns.TripCategory;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Name cannot be null")
    @Size(min = 1, max = 255, message = "Name must be between 1 and 255 characters")
    private String name;

    @NotNull(message = "Start date cannot be null")
    private LocalDate startDate;

    @NotNull(message = "End date cannot be null")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private TripCategory category;

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Task> tasks=new ArrayList<>();
}
