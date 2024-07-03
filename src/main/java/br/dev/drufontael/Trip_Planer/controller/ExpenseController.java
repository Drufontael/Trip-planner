package br.dev.drufontael.Trip_Planer.controller;

import br.dev.drufontael.Trip_Planer.dto.ExpenseDto;
import br.dev.drufontael.Trip_Planer.service.ExpenseService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trips/{tripId}/tasks/{taskId}/expenses")
@AllArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    public ExpenseDto createExpense(@PathVariable("tripId") Long tripId, @PathVariable("taskId") Long taskId,
                                    @RequestBody ExpenseDto expenseDto) {
        return expenseService.addExpenseToTask(taskId, expenseDto);
    }

    @GetMapping
    public List<ExpenseDto> getAllExpensesByTask(@PathVariable("tripId") Long tripId, @PathVariable("taskId") Long taskId) {
        return expenseService.getExpensesByTask(taskId);
    }

    @GetMapping("/{expenseId}")
    public ExpenseDto getExpenseById(@PathVariable("tripId") Long tripId, @PathVariable("taskId") Long taskId,
                                     @PathVariable Long expenseId) {
        return expenseService.getExpenseById(taskId, expenseId);
    }

    @PutMapping("/{expenseId}")
    public ExpenseDto updateExpenseById(@PathVariable("tripId") Long tripId, @PathVariable("taskId") Long taskId,
                                        @PathVariable Long expenseId, @RequestBody ExpenseDto expenseDto) {
        return expenseService.updateExpense(taskId, expenseId, expenseDto);
    }

    @DeleteMapping("/{expenseId}")
    public Boolean deleteExpenseById(@PathVariable("tripId") Long tripId, @PathVariable("taskId") Long taskId,
                                     @PathVariable Long expenseId) {
        return expenseService.deleteExpenseById(taskId, expenseId);
    }
}
