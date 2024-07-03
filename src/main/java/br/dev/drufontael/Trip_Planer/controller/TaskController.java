package br.dev.drufontael.Trip_Planer.controller;

import br.dev.drufontael.Trip_Planer.dto.TaskDto;
import br.dev.drufontael.Trip_Planer.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trips/{tripId}/tasks")
@AllArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public TaskDto createTask(@PathVariable("tripId") Long tripId, @RequestBody TaskDto taskDto) {
        return taskService.addTaskToTrip(tripId, taskDto);
    }

    @GetMapping
    public List<TaskDto> getAllTasksByTrip(@PathVariable("tripId") Long tripId) {
        return taskService.getTasksByTrip(tripId);
    }

    @GetMapping("/{taskId}")
    public TaskDto getTaskById(@PathVariable("tripId") Long tripId, @PathVariable Long taskId) {
        return taskService.getTaskById(tripId, taskId);
    }

    @PutMapping("/{taskId}")
    public TaskDto updateTaskById(@PathVariable("tripId") Long tripId, @PathVariable Long taskId, @RequestBody TaskDto taskDto) {
        return taskService.updateTask(tripId, taskId, taskDto);
    }

    @DeleteMapping("/{taskId}")
    public Boolean deleteTaskById(@PathVariable("tripId") Long tripId, @PathVariable Long taskId) {
        return taskService.deleteTaskById(tripId, taskId);
    }
}
