package br.dev.drufontael.Trip_Planer.service;

import br.dev.drufontael.Trip_Planer.dto.TaskDto;
import br.dev.drufontael.Trip_Planer.exception.ResourceNotFoundException;
import br.dev.drufontael.Trip_Planer.model.Task;
import br.dev.drufontael.Trip_Planer.model.Trip;
import br.dev.drufontael.Trip_Planer.repository.TaskRepository;
import br.dev.drufontael.Trip_Planer.repository.TripRepository;
import br.dev.drufontael.Trip_Planer.utils.Utils;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TripRepository tripRepository;

    @Transactional
    public TaskDto addTaskToTrip(Long tripId, TaskDto taskDto) {
        return tripRepository.findById(tripId).map(trip -> {
            Task task = new Task();
            task.setDescription(taskDto.getDescription());
            task.setDate(taskDto.getDate());
            task.setTrip(trip);
            return toDto(taskRepository.save(task));
        }).orElseThrow(() -> new ResourceNotFoundException("Trip not found with id " + tripId));
    }

    public List<TaskDto> getTasksByTrip(Long tripId) {
        Trip trip= tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found with id " + tripId));
        return taskRepository.findByTripOrderByDateAsc(trip).stream().map(this::toDto).toList();
    }

    public TaskDto getTaskById(Long tripId, Long taskId) {
        if (!tripRepository.existsById(tripId)) {
            throw new ResourceNotFoundException("Trip not found with id " + tripId);
        }
        return taskRepository.findById(taskId).map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + taskId));
    }

    public TaskDto updateTask(Long tripId, Long taskId, TaskDto taskDto) {
        if (!tripRepository.existsById(tripId)) {
            throw new ResourceNotFoundException("Trip not found with id " + tripId);
        }
        return taskRepository.findById(taskId).map(task -> {
            Utils.copyNonNullProperties(taskDto,task);
            return toDto(taskRepository.save(task));
        }).orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + taskId));
    }

    public Boolean deleteTaskById(Long tripId, Long taskId) {
        if (!tripRepository.existsById(tripId)) {
            throw new ResourceNotFoundException("Trip not found with id " + tripId);
        }
        if (taskRepository.existsById(taskId)) {
            taskRepository.deleteById(taskId);
            return true;
        }
        return false;
    }

    private TaskDto toDto(Task task) {
        return new TaskDto(task.getDescription(), task.getDate());
    }
}