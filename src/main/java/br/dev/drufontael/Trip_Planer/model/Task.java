package br.dev.drufontael.Trip_Planer.model;

import jakarta.persistence.*;
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


    private String description;
    private boolean completed;
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "trip_id")
    private Trip trip;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

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
