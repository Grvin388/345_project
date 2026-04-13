package com.ticketapp.backend.controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import tools.jackson.databind.ObjectMapper;
import com.ticketapp.backend.config.FirebaseConfig;
import com.ticketapp.backend.dto.CreateReservationRequest;
import com.ticketapp.backend.model.Reservation;
import com.ticketapp.backend.service.ReservationService;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
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

    private MockedStatic<FirebaseAuth> mockFirebaseAuth(String tokenValue, String userUid, String userEmail)
            throws Exception {
        MockedStatic<FirebaseAuth> firebaseAuthStatic = mockStatic(FirebaseAuth.class);
        FirebaseAuth firebaseAuth = mock(FirebaseAuth.class);
        FirebaseToken firebaseToken = mock(FirebaseToken.class);

        firebaseAuthStatic.when(FirebaseAuth::getInstance).thenReturn(firebaseAuth);
        when(firebaseAuth.verifyIdToken(tokenValue)).thenReturn(firebaseToken);
        when(firebaseToken.getUid()).thenReturn(userUid);
        when(firebaseToken.getEmail()).thenReturn(userEmail);

        return firebaseAuthStatic;
    }

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
    void createReservation_returns200WhenAuthorizationHeaderIsValid() throws Exception {
        CreateReservationRequest request = new CreateReservationRequest();
        request.setEventId(1L);
        request.setQuantity(2);

        Reservation reservation = new Reservation(1L, "uid123", "user@test.com", 2, "CONFIRMED");
        when(reservationService.createReservation(any(CreateReservationRequest.class), eq("uid123"),
                eq("user@test.com")))
                .thenReturn(reservation);

        try (MockedStatic<FirebaseAuth> ignored = mockFirebaseAuth("valid-token", "uid123", "user@test.com")) {
            mockMvc.perform(post("/api/reservations")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer valid-token")
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.userUid").value("uid123"))
                    .andExpect(jsonPath("$.status").value("CONFIRMED"));
        }
    }

    @Test
    void createReservation_returns500WhenTokenVerificationFails() throws Exception {
        try (MockedStatic<FirebaseAuth> firebaseAuthStatic = mockStatic(FirebaseAuth.class)) {
            FirebaseAuth firebaseAuth = mock(FirebaseAuth.class);
            firebaseAuthStatic.when(FirebaseAuth::getInstance).thenReturn(firebaseAuth);
            when(firebaseAuth.verifyIdToken("bad-token")).thenThrow(new RuntimeException("bad token"));

            mockMvc.perform(post("/api/reservations")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer bad-token")
                    .content("{\"eventId\":1,\"quantity\":2}"))
                    .andExpect(status().isInternalServerError())
                    .andExpect(content().string("Reservation failed: bad token"));
        }
    }

    @Test
    void getMyReservations_returns401WhenAuthorizationHeaderIsMissing() throws Exception {
        mockMvc.perform(get("/api/reservations/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getMyReservations_returns200WhenAuthorizationHeaderIsValid() throws Exception {
        Reservation reservation = new Reservation(1L, "uid123", "user@test.com", 2, "CONFIRMED");
        when(reservationService.getReservationsByUserUid("uid123")).thenReturn(List.of(reservation));

        try (MockedStatic<FirebaseAuth> ignored = mockFirebaseAuth("valid-token", "uid123", "user@test.com")) {
            mockMvc.perform(get("/api/reservations/me")
                    .header("Authorization", "Bearer valid-token"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].userUid").value("uid123"))
                    .andExpect(jsonPath("$[0].status").value("CONFIRMED"));
        }
    }

    @Test
    void getMyReservations_returns500WhenServiceThrows() throws Exception {
        when(reservationService.getReservationsByUserUid("uid123")).thenThrow(new RuntimeException("lookup failed"));

        try (MockedStatic<FirebaseAuth> ignored = mockFirebaseAuth("valid-token", "uid123", "user@test.com")) {
            mockMvc.perform(get("/api/reservations/me")
                    .header("Authorization", "Bearer valid-token"))
                    .andExpect(status().isInternalServerError())
                    .andExpect(content().string("Failed to fetch reservations: lookup failed"));
        }
    }

    @Test
    void cancelReservation_returns401WhenAuthorizationHeaderIsMissing() throws Exception {
        mockMvc.perform(delete("/api/reservations/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void cancelReservation_returns200WhenAuthorizationHeaderIsValid() throws Exception {
        Reservation reservation = new Reservation(1L, "uid123", "user@test.com", 2, "CANCELLED");
        when(reservationService.cancelReservation(1L, "uid123")).thenReturn(reservation);

        try (MockedStatic<FirebaseAuth> ignored = mockFirebaseAuth("valid-token", "uid123", "user@test.com")) {
            mockMvc.perform(delete("/api/reservations/1")
                    .header("Authorization", "Bearer valid-token"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("CANCELLED"));
        }
    }

    @Test
    void cancelReservation_returns500WhenServiceThrows() throws Exception {
        when(reservationService.cancelReservation(1L, "uid123")).thenThrow(new RuntimeException("cancel failed"));

        try (MockedStatic<FirebaseAuth> ignored = mockFirebaseAuth("valid-token", "uid123", "user@test.com")) {
            mockMvc.perform(delete("/api/reservations/1")
                    .header("Authorization", "Bearer valid-token"))
                    .andExpect(status().isInternalServerError())
                    .andExpect(content().string("Failed to cancel reservation: cancel failed"));
        }
    }
}
