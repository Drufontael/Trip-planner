package br.dev.drufontael.Trip_Planer.repository;

import br.dev.drufontael.Trip_Planer.model.Expense;
import br.dev.drufontael.Trip_Planer.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByTaskId(Long id);
}