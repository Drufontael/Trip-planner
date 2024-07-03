package br.dev.drufontael.Trip_Planer.dto;

import br.dev.drufontael.Trip_Planer.model.enuns.ExpenseCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseDto {
    private Long id;
    private BigDecimal estimatedCost;
    private BigDecimal actualCost;
    private ExpenseCategory expenseCategory;
}