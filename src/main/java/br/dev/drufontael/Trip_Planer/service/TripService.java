package br.dev.drufontael.Trip_Planer.service;

import br.dev.drufontael.Trip_Planer.dto.TripDto;
import br.dev.drufontael.Trip_Planer.exception.ResourceNotFoundException;
import br.dev.drufontael.Trip_Planer.model.Trip;
import br.dev.drufontael.Trip_Planer.repository.TripRepository;
import br.dev.drufontael.Trip_Planer.utils.Utils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TripService {

    private final TripRepository tripRepository;

    public TripDto createTrip(TripDto tripDto) {
        Trip trip = new Trip();
        Utils.copyNonNullProperties(tripDto,trip);
        if(!trip.getStartDate().isEqual(trip.getEndDate()) && !trip.getStartDate().isBefore(trip.getEndDate())){
            throw new IllegalArgumentException("start date must be equal to or before the end date");
        }
        return toDto(tripRepository.save(trip));
    }

    public List<TripDto> getAllTrips() {
        return tripRepository.findAll().stream().map(this::toDto).toList();
    }

    public TripDto getTripById(Long id) {
        return tripRepository.findById(id).map(this::toDto).orElseThrow(() ->
                new ResourceNotFoundException("Trip not found with id " + id));
    }

    public TripDto updateTrip(Long id, TripDto tripDto) {
        return tripRepository.findById(id).map(trip -> {
            Utils.copyNonNullProperties(tripDto,trip);
            return toDto(tripRepository.save(trip));
        }).orElseThrow(() -> new ResourceNotFoundException("Trip not found with id " + id));
    }

    public Boolean deleteTripById(Long id) {
        if (tripRepository.existsById(id)) {
            tripRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private TripDto toDto(Trip trip) {
        return new TripDto(trip.getId(),trip.getName(), trip.getStartDate(), trip.getEndDate(), trip.getCategory());
    }
}
