package br.dev.drufontael.Trip_Planer.controller;

import br.dev.drufontael.Trip_Planer.dto.TripDto;
import br.dev.drufontael.Trip_Planer.service.TripService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trips")
@AllArgsConstructor
public class TripController {

    private final TripService tripService;

    @PostMapping
    public TripDto createTrip(@RequestBody TripDto tripDto) {
        return tripService.createTrip(tripDto);
    }

    @GetMapping
    public List<TripDto> getAllTrips() {
        return tripService.getAllTrips();
    }

    @GetMapping("/{id}")
    public TripDto getTripById(@PathVariable Long id) {
        return tripService.getTripById(id);
    }

    @PutMapping("/{id}")
    public TripDto updateTripById(@PathVariable Long id, @RequestBody TripDto tripDto) {
        return tripService.updateTrip(id, tripDto);
    }

    @DeleteMapping("/{id}")
    public Boolean deleteTripById(@PathVariable Long id) {
        return tripService.deleteTripById(id);
    }
}
