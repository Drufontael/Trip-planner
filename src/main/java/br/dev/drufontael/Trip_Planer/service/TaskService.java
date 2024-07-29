package br.dev.drufontael.Trip_Planer.service;

import br.dev.drufontael.Trip_Planer.dto.TaskDto;
import br.dev.drufontael.Trip_Planer.exception.InvalidArgumentFormatException;
import br.dev.drufontael.Trip_Planer.exception.ResourceNotFoundException;
import br.dev.drufontael.Trip_Planer.model.Task;
import br.dev.drufontael.Trip_Planer.model.Trip;
import br.dev.drufontael.Trip_Planer.model.User;
import br.dev.drufontael.Trip_Planer.repository.TaskRepository;
import br.dev.drufontael.Trip_Planer.repository.TripRepository;
import br.dev.drufontael.Trip_Planer.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TripRepository tripRepository;
    private HttpServletRequest request;

    @Transactional
    public TaskDto addTaskToTrip(Long tripId, TaskDto taskDto) {
        return tripRepository.findById(tripId).map(trip -> {
            Task task = new Task();
            task.setDescription(taskDto.getDescription());
            if (taskDto.getDate().isBefore(trip.getStartDate()) || taskDto.getDate().isAfter(trip.getEndDate())) {
                throw new InvalidArgumentFormatException("The date must be between the trip's start date and end date");
            }
            task.setDate(taskDto.getDate());
            task.setTrip(trip);
            User user=getUserFromSession();
            task.setUser(user);
            return toDto(taskRepository.save(task));
        }).orElseThrow(() -> new ResourceNotFoundException("Trip not found with id " + tripId));
    }

    public List<TaskDto> getTasksByTrip(Long tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found with id " + tripId));
        User user=getUserFromSession();
        if (!trip.getUser().getUsername().equals(user.getUsername()))
            throw new ResourceNotFoundException("User not found in trip");
        return taskRepository.findByTripOrderByDateAsc(trip).stream().map(this::toDto).toList();
    }

    public TaskDto getTaskById(Long tripId, Long taskId) {
        if (!tripRepository.existsById(tripId)) {
            throw new ResourceNotFoundException("Trip not found with id " + tripId);
        }


        return taskRepository.findById(taskId).map(task -> {
                    if (!task.getTrip().getId().equals(tripId)) {
                        throw new InvalidArgumentFormatException("There is no task with this ID " + taskId
                                + " for a trip with this ID " + tripId);
                    }
                    User user=getUserFromSession();
                    if (!task.getUser().getUsername().equals(user.getUsername()))
                        throw new ResourceNotFoundException("User not found in trip");
                    return toDto(task);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + taskId));
    }

    public TaskDto updateTask(Long tripId, Long taskId, TaskDto taskDto) {
        if (!tripRepository.existsById(tripId)) {
            throw new ResourceNotFoundException("Trip not found with id " + tripId);
        }
        return taskRepository.findById(taskId).map(task -> {
            if (!task.getTrip().getId().equals(tripId)) {
                throw new InvalidArgumentFormatException("There is no task with this ID " + taskId
                        + " for a trip with this ID " + tripId);
            }
            User user=getUserFromSession();
            if (!task.getUser().getUsername().equals(user.getUsername()))
                throw new ResourceNotFoundException("User not found in trip");
            Utils.copyNonNullProperties(taskDto, task);
            return toDto(taskRepository.save(task));
        }).orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + taskId));
    }

    public Boolean deleteTaskById(Long tripId, Long taskId) {
        if (!tripRepository.existsById(tripId)) {
            throw new ResourceNotFoundException("Trip not found with id " + tripId);
        }
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + taskId));
        if (!task.getTrip().getId().equals(tripId)) {
            throw new InvalidArgumentFormatException("There is no task with this ID " + taskId
                    + " for a trip with this ID " + tripId);
        } else {
            taskRepository.deleteById(taskId);
            return true;
        }
    }

    private User getUserFromSession() {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) throw new ResourceNotFoundException("User not found");
        return user;
    }


    private TaskDto toDto(Task task) {

        return new TaskDto(task.getId(), task.getDescription(), task.getDate(),
                task.totalEstimatedCost(), task.totalExpenses());
    }
}