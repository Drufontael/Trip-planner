package br.dev.drufontael.Trip_Planer.configuration;

import br.dev.drufontael.Trip_Planer.dto.ExpenseDto;
import br.dev.drufontael.Trip_Planer.dto.TaskDto;
import br.dev.drufontael.Trip_Planer.dto.TripDto;
import br.dev.drufontael.Trip_Planer.model.enuns.ExpenseCategory;
import br.dev.drufontael.Trip_Planer.model.enuns.TripCategory;
import br.dev.drufontael.Trip_Planer.service.ExpenseService;
import br.dev.drufontael.Trip_Planer.service.TaskService;
import br.dev.drufontael.Trip_Planer.service.TripService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@Component
@Profile("dev")
public class DataInitializer implements CommandLineRunner {

    private final ExpenseService expenseService;
    private final TaskService taskService;
    private final TripService tripService;
    @Override
    public void run(String... args) throws Exception {
        TripDto viagemNegocios = new TripDto(null,"Reunião",
                LocalDate.of(2024, 12, 10),
                LocalDate.of(2024, 12, 15),
                TripCategory.BUSINESS);

        TripDto viagemTurismo = new TripDto(null,"São Paulo",
                LocalDate.of(2024, 9, 6),
                LocalDate.of(2024, 9, 8),
                TripCategory.TOURISM);

        TripDto viagemFamilia = new TripDto(null,"Nordeste",
                LocalDate.of(2024, 12, 21),
                LocalDate.of(2025, 1, 2),
                TripCategory.VISIT);

        viagemFamilia=tripService.createTrip(viagemFamilia);
        viagemTurismo=tripService.createTrip(viagemTurismo);
        viagemNegocios=tripService.createTrip(viagemNegocios);

        TaskDto viagemFamiliaTask=taskService.addTaskToTrip(viagemFamilia.getId(),
                new TaskDto(null,"Viagem",LocalDate.of(2024, 12, 21),
                        null,null));
        TaskDto praiaFamiliaTask=taskService.addTaskToTrip(viagemFamilia.getId(),
                new TaskDto(null,"Praia",LocalDate.of(2024, 12, 22),
                        null,null));

        expenseService.addExpenseToTask(viagemFamiliaTask.getId(),
                new ExpenseDto(null, BigDecimal.valueOf(2000),BigDecimal.valueOf(0), ExpenseCategory.TRANSPORTATION));
        expenseService.addExpenseToTask(viagemFamiliaTask.getId(),
                new ExpenseDto(null, BigDecimal.valueOf(300),BigDecimal.valueOf(0), ExpenseCategory.FOOD));
        expenseService.addExpenseToTask(praiaFamiliaTask.getId(),
                new ExpenseDto(null, BigDecimal.valueOf(50),BigDecimal.valueOf(0), ExpenseCategory.TRANSPORTATION));
        expenseService.addExpenseToTask(praiaFamiliaTask.getId(),
                new ExpenseDto(null, BigDecimal.valueOf(200),BigDecimal.valueOf(0), ExpenseCategory.FOOD));





    }
}
