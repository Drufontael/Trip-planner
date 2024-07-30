package br.dev.drufontael.Trip_Planer.service;

import br.dev.drufontael.Trip_Planer.dto.ExpenseDto;
import br.dev.drufontael.Trip_Planer.exception.InvalidArgumentFormatException;
import br.dev.drufontael.Trip_Planer.exception.ResourceNotFoundException;
import br.dev.drufontael.Trip_Planer.model.Expense;
import br.dev.drufontael.Trip_Planer.model.Task;
import br.dev.drufontael.Trip_Planer.model.User;
import br.dev.drufontael.Trip_Planer.repository.ExpenseRepository;
import br.dev.drufontael.Trip_Planer.repository.TaskRepository;
import br.dev.drufontael.Trip_Planer.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final TaskRepository taskRepository;
    private HttpServletRequest request;

    @Transactional
    public ExpenseDto addExpenseToTask(Long tripId, Long taskId, ExpenseDto expenseDto) {
        return taskRepository.findById(taskId).map(task -> {
            if (!task.getTrip().getId().equals(tripId)){
                throw new ResourceNotFoundException("task does not belong to the trip");
            }
            Expense expense = new Expense();
            Utils.copyNonNullProperties(expenseDto,expense);
            expense.setTask(task);
            User user=getUserFromSession();
            expense.setUser(user);
            task.getExpenses().add(expense);
            taskRepository.save(task);
            return toDto(expenseRepository.save(expense));
        }).orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + taskId));
    }

    public List<ExpenseDto> getExpensesByTask(Long tripId,Long taskId) {
        return taskRepository.findById(taskId).map(task -> {
            if (!task.getTrip().getId().equals(tripId)){
                throw new ResourceNotFoundException("task does not belong to the trip");
            }
            if (!task.getUser().getUsername().equals(getUserFromSession().getUsername()))
                throw new ResourceNotFoundException("task does not belong to the user");
            return expenseRepository.findByTaskId(task.getId()).stream().map(this::toDto).toList();
        }).orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + taskId));
    }

    public ExpenseDto getExpenseById(Long tripId,Long taskId, Long expenseId) {


        return taskRepository.findById(taskId).map(task -> {
            if (!task.getTrip().getId().equals(tripId)){
                throw new ResourceNotFoundException("task does not belong to the trip");
            }
            if (!task.getUser().getUsername().equals(getUserFromSession().getUsername()))
                throw new ResourceNotFoundException("task does not belong to the user");
            Expense expense=expenseRepository.findById(expenseId)
                    .orElseThrow(() -> new ResourceNotFoundException("Expense not found with id " + expenseId));
            if(!expense.getTask().getId().equals(taskId))
                throw new InvalidArgumentFormatException("There is no expense with this ID "+expenseId
                        +" for a task with this ID "+taskId);
            return toDto(expense);

        }).orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + taskId));
    }

    public ExpenseDto updateExpense(Long tripId,Long taskId, Long expenseId, ExpenseDto expenseDto) {
        return taskRepository.findById(taskId).map(task -> {
            if (!task.getTrip().getId().equals(tripId)){
                throw new ResourceNotFoundException("task does not belong to the trip");
            }
            if (!task.getUser().getUsername().equals(getUserFromSession().getUsername()))
                throw new ResourceNotFoundException("task does not belong to the user");
            return expenseRepository.findById(expenseId).map(expense -> {
                Utils.copyNonNullProperties(expenseDto,expense);
                return toDto(expenseRepository.save(expense));
            }).orElseThrow(() -> new ResourceNotFoundException("Expense not found with id " + expenseId));
        }).orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + taskId));
    }

    public Boolean deleteExpenseById(Long tripId,Long taskId, Long expenseId) {
        return taskRepository.findById(taskId).map(task -> {
            if (!task.getTrip().getId().equals(tripId)){
                throw new ResourceNotFoundException("task does not belong to the trip");
            }
            if (!task.getUser().getUsername().equals(getUserFromSession().getUsername()))
                throw new ResourceNotFoundException("task does not belong to the user");
            return expenseRepository.findById(expenseId).map(expense -> {
                if (!expense.getTask().getId().equals(taskId))
                    throw new InvalidArgumentFormatException("There is no expense with this ID "+expenseId
                            +" for a task with this ID "+taskId);
                expenseRepository.deleteById(expenseId);
                return true;
            }).orElse(false);
        }).orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + taskId));
    }

    private User getUserFromSession() {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) throw new ResourceNotFoundException("There is no open session");
        return user;
    }

    private ExpenseDto toDto(Expense expense) {
        return new ExpenseDto(expense.getId(),expense.getEstimatedCost(), expense.getActualCost(), expense.getExpenseCategory());
    }
}
