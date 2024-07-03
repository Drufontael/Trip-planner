package br.dev.drufontael.Trip_Planer.service;

import br.dev.drufontael.Trip_Planer.dto.ExpenseDto;
import br.dev.drufontael.Trip_Planer.exception.ResourceNotFoundException;
import br.dev.drufontael.Trip_Planer.model.Expense;
import br.dev.drufontael.Trip_Planer.repository.ExpenseRepository;
import br.dev.drufontael.Trip_Planer.repository.TaskRepository;
import br.dev.drufontael.Trip_Planer.utils.Utils;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final TaskRepository taskRepository;

    @Transactional
    public ExpenseDto addExpenseToTask(Long taskId, ExpenseDto expenseDto) {
        return taskRepository.findById(taskId).map(task -> {
            Expense expense = new Expense();
            Utils.copyNonNullProperties(expenseDto,expense);
            return toDto(expenseRepository.save(expense));
        }).orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + taskId));
    }

    public List<ExpenseDto> getExpensesByTask(Long taskId) {
        return taskRepository.findById(taskId).map(expenseRepository::findByTask)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + taskId))
                .stream().map(this::toDto).toList();
    }

    public ExpenseDto getExpenseById(Long taskId, Long expenseId) {
        if (!taskRepository.existsById(taskId)) {
            throw new ResourceNotFoundException("Task not found with id " + taskId);
        }
        return expenseRepository.findById(expenseId).map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found with id " + expenseId));
    }

    public ExpenseDto updateExpense(Long taskId, Long expenseId, ExpenseDto expenseDto) {
        if (!taskRepository.existsById(taskId)) {
            throw new ResourceNotFoundException("Task not found with id " + taskId);
        }
        return expenseRepository.findById(expenseId).map(expense -> {
            Utils.copyNonNullProperties(expenseDto,expense);
            return toDto(expenseRepository.save(expense));
        }).orElseThrow(() -> new ResourceNotFoundException("Expense not found with id " + expenseId));
    }

    public Boolean deleteExpenseById(Long taskId, Long expenseId) {
        if (!taskRepository.existsById(taskId)) {
            throw new ResourceNotFoundException("Task not found with id " + taskId);
        }
        if (expenseRepository.existsById(expenseId)) {
            expenseRepository.deleteById(expenseId);
            return true;
        }
        return false;
    }

    private ExpenseDto toDto(Expense expense) {
        return new ExpenseDto(expense.getEstimatedCost(), expense.getActualCost(), expense.getExpenseCategory());
    }
}