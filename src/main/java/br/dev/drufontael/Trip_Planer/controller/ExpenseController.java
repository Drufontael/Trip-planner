package br.dev.drufontael.Trip_Planer.controller;

import br.dev.drufontael.Trip_Planer.dto.ExpenseDto;
import br.dev.drufontael.Trip_Planer.service.ExpenseService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/trips/{tripId}/tasks/{taskId}/expenses")
@AllArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<ExpenseDto> createExpense(@PathVariable("tripId") Long tripId, @PathVariable("taskId") Long taskId,
                                                   @Valid @RequestBody ExpenseDto expenseDto) {
        ExpenseDto newExpenseDto=expenseService.addExpenseToTask(taskId, expenseDto);
        URI location= ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(newExpenseDto.getId()).toUri();
        return ResponseEntity.created(location).body(newExpenseDto);
    }

    @GetMapping
    public ResponseEntity<List<ExpenseDto>> getAllExpensesByTask(@PathVariable("tripId") Long tripId, @PathVariable("taskId") Long taskId) {
        return ResponseEntity.ok().body(expenseService.getExpensesByTask(taskId));
    }

    @GetMapping("/{expenseId}")
    public ResponseEntity<ExpenseDto> getExpenseById(@PathVariable("tripId") Long tripId, @PathVariable("taskId") Long taskId,
                                     @PathVariable Long expenseId) {
        return ResponseEntity.ok().body(expenseService.getExpenseById(taskId, expenseId));
    }

    @PutMapping("/{expenseId}")
    public ResponseEntity<ExpenseDto> updateExpenseById(@PathVariable("tripId") Long tripId, @PathVariable("taskId") Long taskId,
                                        @PathVariable Long expenseId, @RequestBody ExpenseDto expenseDto) {
        return ResponseEntity.ok().body(expenseService.updateExpense(taskId, expenseId, expenseDto));
    }

    @DeleteMapping("/{expenseId}")
    public ResponseEntity<Boolean> deleteExpenseById(@PathVariable("tripId") Long tripId, @PathVariable("taskId") Long taskId,
                                     @PathVariable Long expenseId) {
        return ResponseEntity.ok().body(expenseService.deleteExpenseById(taskId, expenseId));
    }
}
