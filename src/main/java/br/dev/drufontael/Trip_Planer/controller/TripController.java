package br.dev.drufontael.Trip_Planer.controller;

import br.dev.drufontael.Trip_Planer.dto.TripDto;
import br.dev.drufontael.Trip_Planer.service.TripService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/trips")
@AllArgsConstructor
public class TripController {

    private final TripService tripService;

    @PostMapping
    public ResponseEntity<TripDto> createTrip(@RequestBody TripDto tripDto) {
        TripDto newTripDto=tripService.createTrip(tripDto);
        URI location= ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(newTripDto.getId()).toUri();

        return ResponseEntity.created(location).body(newTripDto);
    }

    @GetMapping
    public ResponseEntity<List<TripDto>> getAllTrips() {
        return ResponseEntity.ok().body(tripService.getAllTrips());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TripDto> getTripById(@PathVariable Long id) {
        return ResponseEntity.ok().body(tripService.getTripById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TripDto> updateTripById(@PathVariable Long id, @RequestBody TripDto tripDto) {
        return ResponseEntity.ok().body(tripService.updateTrip(id, tripDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteTripById(@PathVariable Long id) {
        return ResponseEntity.ok().body(tripService.deleteTripById(id));
    }
}
