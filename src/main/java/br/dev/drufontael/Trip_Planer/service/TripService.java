package br.dev.drufontael.Trip_Planer.service;

import br.dev.drufontael.Trip_Planer.dto.TripDto;
import br.dev.drufontael.Trip_Planer.exception.ResourceNotFoundException;
import br.dev.drufontael.Trip_Planer.model.Trip;
import br.dev.drufontael.Trip_Planer.model.User;
import br.dev.drufontael.Trip_Planer.repository.TripRepository;
import br.dev.drufontael.Trip_Planer.repository.UserRepository;
import br.dev.drufontael.Trip_Planer.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TripService {

    private final TripRepository tripRepository;
    private final HttpServletRequest request;
    private final UserRepository userRepository;

    @Transactional
    public TripDto createTrip(TripDto tripDto) {
        Trip trip = new Trip();
        Utils.copyNonNullProperties(tripDto,trip);
        if(!trip.getStartDate().isEqual(trip.getEndDate()) && !trip.getStartDate().isBefore(trip.getEndDate())){
            throw new IllegalArgumentException("start date must be equal to or before the end date");
        }
        User user=getUserFromSession();
        trip.setUser(user);
        Trip savedTrip = tripRepository.save(trip);
        user.getTrips().add(savedTrip);
        userRepository.save(user);
        return toDto(savedTrip);
    }

    public List<TripDto> getAllTrips() {
        User user=getUserFromSession();
        return tripRepository.findByUser(user).stream().map(this::toDto).toList();
    }

    public TripDto getTripById(Long id) {
        return tripRepository.findById(id).map(trip->{
            if(!trip.getUser().getUsername().equals(getUserFromSession().getUsername()))
                throw new ResourceNotFoundException("User not found in trip");
            return toDto(trip);
        }).orElseThrow(() ->
                new ResourceNotFoundException("Trip not found with id " + id));
    }

    public TripDto updateTrip(Long id, TripDto tripDto) {
        return tripRepository.findById(id).map(trip -> {
            if(!trip.getUser().getUsername().equals(getUserFromSession().getUsername()))
                throw new ResourceNotFoundException("User not found in trip");
            Utils.copyNonNullProperties(tripDto,trip);
            return toDto(tripRepository.save(trip));
        }).orElseThrow(() -> new ResourceNotFoundException("Trip not found with id " + id));
    }

    public Boolean deleteTripById(Long id) {
        tripRepository.findById(id).map(trip -> {
            if(!trip.getUser().getUsername().equals(getUserFromSession().getUsername()))
                throw new ResourceNotFoundException("User not found in trip");
            tripRepository.delete(trip);
            return true;
        });
        return false;
    }

    private User getUserFromSession() {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) throw new ResourceNotFoundException("User not found");
        return user;
    }

    private TripDto toDto(Trip trip) {
        return new TripDto(trip.getId(),trip.getName(), trip.getStartDate(), trip.getEndDate(), trip.getCategory());
    }
}
