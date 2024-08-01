package br.dev.drufontael.Trip_Planer.repository;

import br.dev.drufontael.Trip_Planer.model.Trip;
import br.dev.drufontael.Trip_Planer.model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
class TripRepositoryTest {

    @Autowired
    private TripRepository tripRepository;
    @Autowired
    private UserRepository userRepository;
    private User user;





    @Test
    void findByUser() {
        user = new User("admin", "admin", new ArrayList<>());
        userRepository.save(user);
        List<Trip> trips = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            Trip trip = new Trip();
            trip.setUser(user);
            trip.setName("Trip " + i);
            //user.getTrips().add(trip);
            trips.add(trip);
        }
        for (int i=0;i<5;i++){
            trips.add(new Trip());
        }
        tripRepository.saveAll(trips);


        List<Trip> tripsSaved = tripRepository.findByUser(user);
        assertEquals(10, tripsSaved.size());
    }
}