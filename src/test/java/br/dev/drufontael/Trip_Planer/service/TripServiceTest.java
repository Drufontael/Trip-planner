package br.dev.drufontael.Trip_Planer.service;

import br.dev.drufontael.Trip_Planer.dto.TripDto;
import br.dev.drufontael.Trip_Planer.exception.ResourceNotFoundException;
import br.dev.drufontael.Trip_Planer.model.Trip;
import br.dev.drufontael.Trip_Planer.model.User;
import br.dev.drufontael.Trip_Planer.model.enuns.TripCategory;
import br.dev.drufontael.Trip_Planer.repository.TripRepository;
import br.dev.drufontael.Trip_Planer.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TripServiceTest {

    @Mock
    private TripRepository tripRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpSession session;
    @InjectMocks
    private TripService tripService;

    private TripDto tripDto;
    private Trip trip;
    private User user;

    @BeforeEach
    void setUp() {
        tripDto = new TripDto(null, "Trip1",
                LocalDate.of(2022, 1, 1),
                LocalDate.of(2022, 1, 2),
                TripCategory.TOURISM);

        trip = new Trip();
        trip.setId(1L);
        trip.setName("Trip1");
        trip.setStartDate(LocalDate.of(2022, 1, 1));
        trip.setEndDate(LocalDate.of(2022, 1, 2));
        trip.setCategory(TripCategory.TOURISM);
    }

    @Test
    void createTrip() {
        user = new User("admin", "admin", new ArrayList<>());
        Mockito.when(session.getAttribute("user")).thenReturn(user);
        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(tripRepository.save(Mockito.any(Trip.class))).thenReturn(new Trip());

        TripDto savedTrip = tripService.createTrip(tripDto);

        ArgumentCaptor<Trip> tripCaptor = ArgumentCaptor.forClass(Trip.class);
        Mockito.verify(tripRepository).save(tripCaptor.capture());
        Trip savedTripCaptured = tripCaptor.getValue();

        assertEquals(tripDto.getName(), savedTripCaptured.getName());
        assertEquals(tripDto.getStartDate(), savedTripCaptured.getStartDate());
        assertEquals(tripDto.getEndDate(), savedTripCaptured.getEndDate());
        assertEquals(tripDto.getCategory(), savedTripCaptured.getCategory());
        assertNotNull(savedTrip);

        Mockito.verify(tripRepository).save(Mockito.any(Trip.class));
        Mockito.verify(userRepository).save(Mockito.any(User.class));
    }

    @Test
    void createTrip_InvalidDates() {
        tripDto.setStartDate(LocalDate.of(2022, 1, 2));
        tripDto.setEndDate(LocalDate.of(2022, 1, 1));

        assertThrows(IllegalArgumentException.class, () -> tripService.createTrip(tripDto));
    }

    @Test
    void getAllTrips() {
        user = new User("admin", "admin", new ArrayList<>());
        List<Trip> trips = Arrays.asList(new Trip(), new Trip());
        Mockito.when(session.getAttribute("user")).thenReturn(user);
        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(tripRepository.findByUser(Mockito.any(User.class))).thenReturn(trips);

        List<TripDto> savedTrips = tripService.getAllTrips();

        Mockito.verify(tripRepository).findByUser(Mockito.any(User.class));
        assertEquals(2, savedTrips.size());
        assertTrue(savedTrips.get(0) instanceof TripDto);
    }

    @Test
    void getTripById() {
        user = new User("admin", "admin", new ArrayList<>());
        trip.setUser(user);
        Mockito.when(session.getAttribute("user")).thenReturn(user);
        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(tripRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(trip));

        TripDto savedTrip = tripService.getTripById(1L);

        Mockito.verify(tripRepository).findById(Mockito.anyLong());
        assertEquals(trip.getId(), savedTrip.getId());
        assertEquals(trip.getName(), savedTrip.getName());
        assertEquals(trip.getStartDate(), savedTrip.getStartDate());
        assertEquals(trip.getEndDate(), savedTrip.getEndDate());
        assertEquals(trip.getCategory(), savedTrip.getCategory());
    }

    @Test
    void getTripById_NotFound() {
        Mockito.when(tripRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> tripService.getTripById(1L));
    }

    @Test
    void getTripById_UserNotAuthorized() {
        user = new User("admin", "admin", new ArrayList<>());
        trip.setUser(new User("testUser", "password", new ArrayList<>()));
        Mockito.when(session.getAttribute("user")).thenReturn(user);
        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(tripRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(trip));

        assertThrows(ResourceNotFoundException.class, () -> tripService.getTripById(1L));
    }

    @Test
    void updateTrip() {
        user = new User("admin", "admin", new ArrayList<>());
        trip.setUser(user);

        TripDto updatedTripDto = new TripDto();
        updatedTripDto.setName("Trip1 alterado");
        updatedTripDto.setEndDate(LocalDate.of(2022, 1, 3));

        Mockito.when(session.getAttribute("user")).thenReturn(user);
        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(tripRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(trip));
        Mockito.when(tripRepository.save(Mockito.any(Trip.class))).thenReturn(trip);

        TripDto savedTrip = tripService.updateTrip(1L, updatedTripDto);

        Mockito.verify(tripRepository).save(Mockito.any(Trip.class));
        assertEquals("Trip1 alterado", savedTrip.getName());
        assertEquals(LocalDate.of(2022, 1, 3), savedTrip.getEndDate());
    }

    @Test
    void updateTrip_NotFound() {
        TripDto updatedTripDto = new TripDto();
        Mockito.when(tripRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> tripService.updateTrip(1L, updatedTripDto));
    }

    @Test
    void updateTrip_UserNotAuthorized() {
        user = new User("admin", "admin", new ArrayList<>());
        trip.setUser(new User("testUser", "password", new ArrayList<>()));
        Mockito.when(session.getAttribute("user")).thenReturn(user);
        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(tripRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(trip));

        assertThrows(ResourceNotFoundException.class, () -> tripService.updateTrip(1L, new TripDto()));
    }

    @Test
    void deleteTripById() {
        user = new User("admin", "admin", new ArrayList<>());
        trip.setUser(user);
        Mockito.when(session.getAttribute("user")).thenReturn(user);
        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(tripRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(trip));
        Mockito.doNothing().when(tripRepository).delete(trip);

        Boolean deleted = tripService.deleteTripById(1L);

        Mockito.verify(tripRepository).delete(trip);
        assertTrue(deleted);
    }

    @Test
    void deleteTripById_NotFound() {
        Mockito.when(tripRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        Boolean deleted = tripService.deleteTripById(1L);

        Mockito.verify(tripRepository, Mockito.never()).delete(Mockito.any(Trip.class));
        assertFalse(deleted);
    }

    @Test
    void deleteTripById_UserNotAuthorized() {
        user = new User("admin", "admin", new ArrayList<>());
        trip.setUser(new User("testUser", "password", new ArrayList<>()));
        Mockito.when(session.getAttribute("user")).thenReturn(user);
        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(tripRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(trip));

        assertThrows(ResourceNotFoundException.class, () -> tripService.deleteTripById(1L));
    }
}
