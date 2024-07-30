package br.dev.drufontael.Trip_Planer.configuration;

import br.dev.drufontael.Trip_Planer.model.Expense;
import br.dev.drufontael.Trip_Planer.model.Task;
import br.dev.drufontael.Trip_Planer.model.Trip;
import br.dev.drufontael.Trip_Planer.model.User;
import br.dev.drufontael.Trip_Planer.model.enuns.ExpenseCategory;
import br.dev.drufontael.Trip_Planer.model.enuns.TripCategory;
import br.dev.drufontael.Trip_Planer.repository.ExpenseRepository;
import br.dev.drufontael.Trip_Planer.repository.TaskRepository;
import br.dev.drufontael.Trip_Planer.repository.TripRepository;
import br.dev.drufontael.Trip_Planer.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

@AllArgsConstructor
@Component
@Profile("dev")
public class DataInitializer implements CommandLineRunner {

    private final ExpenseRepository expenseRepository;
    private final TaskRepository taskRepository;
    private final TripRepository tripRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        // Inicializar o usuário
        User user = new User();
        user.setUsername("admin");
        user.setPassword(passwordEncoder.encode("admin"));
        userRepository.save(user);

        // Inicializar viagens
        Trip viagemNegocios = new Trip();
        viagemNegocios.setName("Reunião");
        viagemNegocios.setStartDate(LocalDate.of(2024, 12, 10));
        viagemNegocios.setEndDate(LocalDate.of(2024, 12, 15));
        viagemNegocios.setCategory(TripCategory.BUSINESS);
        viagemNegocios.setUser(user);

        Trip viagemTurismo = new Trip();
        viagemTurismo.setName("São Paulo");
        viagemTurismo.setStartDate(LocalDate.of(2024, 9, 6));
        viagemTurismo.setEndDate(LocalDate.of(2024, 9, 8));
        viagemTurismo.setCategory(TripCategory.TOURISM);
        viagemTurismo.setUser(user);

        Trip viagemFamilia = new Trip();
        viagemFamilia.setName("Nordeste");
        viagemFamilia.setStartDate(LocalDate.of(2024, 12, 21));
        viagemFamilia.setEndDate(LocalDate.of(2025, 1, 2));
        viagemFamilia.setCategory(TripCategory.VISIT);
        viagemFamilia.setUser(user);

        viagemFamilia = tripRepository.save(viagemFamilia);
        viagemTurismo = tripRepository.save(viagemTurismo);
        viagemNegocios = tripRepository.save(viagemNegocios);

        // Inicializar tarefas
        Task viagemFamiliaTask = new Task();
        viagemFamiliaTask.setDescription("Viagem");
        viagemFamiliaTask.setDate(LocalDate.of(2024, 12, 21));
        viagemFamiliaTask.setTrip(viagemFamilia);
        viagemFamiliaTask.setUser(user);

        Task praiaFamiliaTask = new Task();
        praiaFamiliaTask.setDescription("Praia");
        praiaFamiliaTask.setDate(LocalDate.of(2024, 12, 22));
        praiaFamiliaTask.setTrip(viagemFamilia);
        praiaFamiliaTask.setUser(user);

        viagemFamiliaTask = taskRepository.save(viagemFamiliaTask);
        praiaFamiliaTask = taskRepository.save(praiaFamiliaTask);

        // Inicializar despesas
        Expense expense1 = new Expense();
        expense1.setEstimatedCost(BigDecimal.valueOf(2000));
        expense1.setActualCost(BigDecimal.valueOf(0));
        expense1.setExpenseCategory(ExpenseCategory.TRANSPORTATION);
        expense1.setTask(viagemFamiliaTask);
        expense1.setUser(user);

        Expense expense2 = new Expense();
        expense2.setEstimatedCost(BigDecimal.valueOf(300));
        expense2.setActualCost(BigDecimal.valueOf(0));
        expense2.setExpenseCategory(ExpenseCategory.FOOD);
        expense2.setTask(viagemFamiliaTask);
        expense2.setUser(user);

        Expense expense3 = new Expense();
        expense3.setEstimatedCost(BigDecimal.valueOf(50));
        expense3.setActualCost(BigDecimal.valueOf(0));
        expense3.setExpenseCategory(ExpenseCategory.TRANSPORTATION);
        expense3.setTask(praiaFamiliaTask);
        expense3.setUser(user);

        Expense expense4 = new Expense();
        expense4.setEstimatedCost(BigDecimal.valueOf(200));
        expense4.setActualCost(BigDecimal.valueOf(0));
        expense4.setExpenseCategory(ExpenseCategory.FOOD);
        expense4.setTask(praiaFamiliaTask);
        expense4.setUser(user);

        expenseRepository.saveAll(Arrays.asList(expense1, expense2, expense3, expense4));
    }
}
