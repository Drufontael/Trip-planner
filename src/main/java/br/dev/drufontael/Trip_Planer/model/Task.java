package br.dev.drufontael.Trip_Planer.model;

import br.dev.drufontael.Trip_Planer.model.enuns.ExpenseCategory;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
public class Task implements Comparable<Task> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private boolean completed;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal estimatedCost = BigDecimal.valueOf(0.00);

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal actualCost = BigDecimal.valueOf(0.00);

    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private ExpenseCategory expenseCategory;

    @ManyToOne
    @JoinColumn(name = "trip_id")
    private Trip trip;

    // Getters and Setters

    @Override
    public int compareTo(Task other) {
        return this.date.compareTo(other.date);
    }
}
