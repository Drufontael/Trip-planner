package br.dev.drufontael.Trip_Planer.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    @DisplayName("Should return total expenses")
    void totalExpenses() {
        Task task = new Task();
        task.setExpenses(new ArrayList<>());
        BigDecimal total = task.totalExpenses();
        assertEquals(BigDecimal.valueOf(0.00), total);
        Expense expense = new Expense();
        expense.setActualCost(BigDecimal.valueOf(10.00));
        task.getExpenses().add(expense);
        total = task.totalExpenses();
        assertEquals(BigDecimal.valueOf(10.00), total);
        Expense expense2 = new Expense();
        expense2.setActualCost(BigDecimal.valueOf(20.00));
        task.getExpenses().add(expense2);
        total = task.totalExpenses();
        assertEquals(BigDecimal.valueOf(30.00), total);
    }

    @Test
    @DisplayName("Should return total estimated cost")
    void totalEstimatedCost() {
        Task task = new Task();
        task.setExpenses(new ArrayList<>());
        BigDecimal total = task.totalEstimatedCost();
        assertEquals(BigDecimal.valueOf(0.00), total);
        Expense expense = new Expense();
        expense.setEstimatedCost(BigDecimal.valueOf(10.00));
        task.getExpenses().add(expense);
        total = task.totalEstimatedCost();
        assertEquals(BigDecimal.valueOf(10.00), total);
        Expense expense2 = new Expense();
        expense2.setEstimatedCost(BigDecimal.valueOf(20.00));
        task.getExpenses().add(expense2);
        total = task.totalEstimatedCost();
        assertEquals(BigDecimal.valueOf(30.00), total);
    }
}