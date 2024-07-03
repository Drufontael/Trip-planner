package br.dev.drufontael.Trip_Planer.controller;

import br.dev.drufontael.Trip_Planer.dto.TaskDto;
import br.dev.drufontael.Trip_Planer.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/trips/{tripId}/tasks")
@AllArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskDto> createTask(@PathVariable("tripId") Long tripId, @RequestBody TaskDto taskDto) {
        TaskDto newTaskDto=taskService.addTaskToTrip(tripId, taskDto);
        URI location= ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(newTaskDto.getId()).toUri();
        return ResponseEntity.created(location).body(newTaskDto);
    }

    @GetMapping
    public ResponseEntity<List<TaskDto>> getAllTasksByTrip(@PathVariable("tripId") Long tripId) {
        return ResponseEntity.ok().body(taskService.getTasksByTrip(tripId));
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable("tripId") Long tripId, @PathVariable Long taskId) {
        return ResponseEntity.ok().body(taskService.getTaskById(tripId, taskId));
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<TaskDto> updateTaskById(@PathVariable("tripId") Long tripId, @PathVariable Long taskId, @RequestBody TaskDto taskDto) {
        return ResponseEntity.ok().body(taskService.updateTask(tripId, taskId, taskDto));
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Boolean> deleteTaskById(@PathVariable("tripId") Long tripId, @PathVariable Long taskId) {
        return ResponseEntity.ok().body(taskService.deleteTaskById(tripId, taskId));
    }
}
