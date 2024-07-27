package br.dev.drufontael.Trip_Planer.controller;

import br.dev.drufontael.Trip_Planer.model.enuns.ExpenseCategory;
import br.dev.drufontael.Trip_Planer.model.enuns.TripCategory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("api/categories")
public class CategoriesController {

    @GetMapping("/trips")
    public ResponseEntity<List<String>> tripCategories() {
        return ResponseEntity.ok().body(Arrays.stream(TripCategory.values()).map(TripCategory::name).toList());
    }

    @GetMapping("/expenses")
    public ResponseEntity<List<String>> expenseCategories() {
        return ResponseEntity.ok().body(Arrays.stream(ExpenseCategory.values()).map(ExpenseCategory::name).toList());
    }
}
