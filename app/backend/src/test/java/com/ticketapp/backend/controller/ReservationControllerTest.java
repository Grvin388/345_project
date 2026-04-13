package com.ticketapp.backend.controller;

import tools.jackson.databind.ObjectMapper;
import com.ticketapp.backend.config.FirebaseConfig;
import com.ticketapp.backend.model.Reservation;
import com.ticketapp.backend.service.ReservationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReservationController.class)
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReservationService reservationService;

    // Prevent FirebaseConfig @PostConstruct from running in the MVC slice context
    @MockitoBean
    private FirebaseConfig firebaseConfig;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllReservations_returns200WithList() throws Exception {
        Reservation r = new Reservation(1L, "uid123", "user@test.com", 2, "CONFIRMED");
        when(reservationService.getAllReservations()).thenReturn(List.of(r));

        mockMvc.perform(get("/api/reservations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userUid").value("uid123"))
                .andExpect(jsonPath("$[0].status").value("CONFIRMED"));
    }

    @Test
    void createReservation_returns401WhenAuthorizationHeaderIsMissing() throws Exception {
        mockMvc.perform(post("/api/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"eventId\":1,\"quantity\":2}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createReservation_returns401WhenAuthorizationHeaderIsInvalid() throws Exception {
        mockMvc.perform(post("/api/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "InvalidToken")
                .content("{\"eventId\":1,\"quantity\":2}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getMyReservations_returns401WhenAuthorizationHeaderIsMissing() throws Exception {
        mockMvc.perform(get("/api/reservations/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void cancelReservation_returns401WhenAuthorizationHeaderIsMissing() throws Exception {
        mockMvc.perform(delete("/api/reservations/1"))
                .andExpect(status().isUnauthorized());
    }
}
