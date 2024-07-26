package br.dev.drufontael.Trip_Planer.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Task implements Comparable<Task> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Description cannot be null")
    @Size(min = 1, max = 255, message = "Description must be between 1 and 255 characters")
    private String description;
    private boolean completed;

    @NotNull(message = "Date cannot be null")
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "trip_id")
    private Trip trip;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Expense> expenses = new ArrayList<>();

    public BigDecimal totalEstimatedCost() {
        BigDecimal total = BigDecimal.valueOf(0.00);
        for (Expense expense : expenses) {
            total = total.add(expense.getEstimatedCost());
        }
        return total;
    }
    public BigDecimal totalExpenses() {
        BigDecimal total = BigDecimal.valueOf(0.00);
        for (Expense expense : expenses) {
            total = total.add(expense.getActualCost());
        }
        return total;
    }

    @Override
    public int compareTo(Task other) {
        return this.date.compareTo(other.date);
    }
}
