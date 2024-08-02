package br.dev.drufontael.Trip_Planer.controller;

import br.dev.drufontael.Trip_Planer.configuration.WebSecurityConfig;
import br.dev.drufontael.Trip_Planer.dto.TripDto;
import br.dev.drufontael.Trip_Planer.model.enuns.TripCategory;
import br.dev.drufontael.Trip_Planer.service.TripService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TripController.class)
@Import(WebSecurityConfig.class)
class TripControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TripService tripService;

    @Test
    void createTrip() throws Exception {
        TripDto tripDto = new TripDto(null,
                "Trip 1",
                LocalDate.of(2022, 1, 1),
                LocalDate.of(2022, 1, 1),
                TripCategory.TOURISM);

        TripDto savedTripDto = new TripDto(1L,
                "Trip 1",
                LocalDate.of(2022, 1, 1),
                LocalDate.of(2022, 1, 1),
                TripCategory.TOURISM);

        Mockito.when(tripService.createTrip(tripDto)).thenReturn(savedTripDto);

        mockMvc.perform(post("/api/trips")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Trip 1\",\"startDate\":\"2022-01-01\",\"endDate\":\"2022-01-01\",\"category\":\"TOURISM\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().string("{\"id\":1,\"name\":\"Trip 1\",\"startDate\":\"2022-01-01\",\"endDate\":\"2022-01-01\",\"category\":\"TOURISM\"}"));
    }

    @Test
    void createTrip_NameNull() throws Exception {
        mockMvc.perform(post("/api/trips")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":null,\"startDate\":\"2022-01-01\",\"endDate\":\"2022-01-01\",\"category\":\"TOURISM\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createTrip_NameEmpty() throws Exception {
        mockMvc.perform(post("/api/trips")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\",\"startDate\":\"2022-01-01\",\"endDate\":\"2022-01-01\",\"category\":\"TOURISM\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createTrip_CategoryNull() throws Exception {
        mockMvc.perform(post("/api/trips")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Trip 1\",\"startDate\":\"2022-01-01\",\"endDate\":\"2022-01-01\",\"category\":null}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createTrip_CategoryEmpty() throws Exception {
        mockMvc.perform(post("/api/trips")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Trip 1\",\"startDate\":\"2022-01-01\",\"endDate\":\"2022-01-01\",\"category\":\"\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllTrips() throws Exception {
        List<TripDto> tripDtos = List.of(
                new TripDto(1L,
                        "Trip 1",
                        LocalDate.of(2022, 1, 1),
                        LocalDate.of(2022, 1, 1),
                        TripCategory.TOURISM),
                new TripDto(2L,
                        "Trip 2",
                        LocalDate.of(2022, 1, 1),
                        LocalDate.of(2022, 1, 1),
                        TripCategory.VISIT),
                new TripDto(3L,
                        "Trip 3",
                        LocalDate.of(2022, 1, 1),
                        LocalDate.of(2022, 1, 1),
                        TripCategory.BUSINESS)
        );

        Mockito.when(tripService.getAllTrips()).thenReturn(tripDtos);

        mockMvc.perform(get("/api/trips"))
                .andExpect(status().isOk())
                .andExpect(content().string("[{\"id\":1,\"name\":\"Trip 1\",\"startDate\":\"2022-01-01\",\"endDate\":\"2022-01-01\",\"category\":\"TOURISM\"}," +
                        "{\"id\":2,\"name\":\"Trip 2\",\"startDate\":\"2022-01-01\",\"endDate\":\"2022-01-01\",\"category\":\"VISIT\"}," +
                        "{\"id\":3,\"name\":\"Trip 3\",\"startDate\":\"2022-01-01\",\"endDate\":\"2022-01-01\",\"category\":\"BUSINESS\"}]"));
    }

    @Test
    void getTripById() throws Exception {
        TripDto tripDto = new TripDto(1L,
                "Trip 1",
                LocalDate.of(2022, 1, 1),
                LocalDate.of(2022, 1, 1),
                TripCategory.TOURISM);

        Mockito.when(tripService.getTripById(1L)).thenReturn(tripDto);

        mockMvc.perform(get("/api/trips/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"id\":1,\"name\":\"Trip 1\",\"startDate\":\"2022-01-01\",\"endDate\":\"2022-01-01\",\"category\":\"TOURISM\"}"));
    }

    @Test
    void updateTripById() throws Exception {
        TripDto tripDto = new TripDto(null,
                null,
                LocalDate.of(2022, 1, 1),
                LocalDate.of(2022, 1, 1),
                null);
        TripDto updatedTripDto = new TripDto(1L,
                "Trip 1",
                LocalDate.of(2022, 1, 1),
                LocalDate.of(2022, 1, 1),
                TripCategory.TOURISM);
        Mockito.when(tripService.updateTrip(1L, tripDto)).thenReturn(updatedTripDto);

        mockMvc.perform(put("/api/trips/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"startDate\":\"2022-01-01\",\"endDate\":\"2022-01-01\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"id\":1,\"name\":\"Trip 1\",\"startDate\":\"2022-01-01\",\"endDate\":\"2022-01-01\",\"category\":\"TOURISM\"}"));


    }

    @Test
    void deleteTripById() throws Exception {
        Mockito.when(tripService.deleteTripById(1L)).thenReturn(true);
        mockMvc.perform(delete("/api/trips/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}