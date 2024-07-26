package br.dev.drufontael.Trip_Planer.model;

import br.dev.drufontael.Trip_Planer.model.enuns.ExpenseCategory;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal estimatedCost = BigDecimal.valueOf(0.00);

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal actualCost = BigDecimal.valueOf(0.00);

    @Enumerated(EnumType.STRING)
    private ExpenseCategory expenseCategory;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;
}
