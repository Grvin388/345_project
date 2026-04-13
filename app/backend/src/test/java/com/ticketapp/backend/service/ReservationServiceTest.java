package com.ticketapp.backend.service;

import com.ticketapp.backend.dto.CreateReservationRequest;
import com.ticketapp.backend.model.Reservation;
import com.ticketapp.backend.repository.ReservationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private ReservationService reservationService;

    private CreateReservationRequest buildRequest(Long eventId, int quantity) {
        CreateReservationRequest r = new CreateReservationRequest();
        r.setEventId(eventId);
        r.setQuantity(quantity);
        return r;
    }

    @Test
    void createReservation_savesWithConfirmedStatus() {
        CreateReservationRequest request = buildRequest(1L, 2);
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(inv -> inv.getArgument(0));

        Reservation result = reservationService.createReservation(request, "uid123", "user@test.com");

        assertThat(result.getStatus()).isEqualTo("CONFIRMED");
        assertThat(result.getUserUid()).isEqualTo("uid123");
        assertThat(result.getEventId()).isEqualTo(1L);
        assertThat(result.getQuantity()).isEqualTo(2);
    }

    @Test
    void createReservation_sendsEmailWhenEmailIsPresent() {
        CreateReservationRequest request = buildRequest(1L, 2);
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(inv -> inv.getArgument(0));

        reservationService.createReservation(request, "uid123", "user@test.com");

        verify(emailService).sendReservationConfirmation(eq("user@test.com"), any(Reservation.class));
    }

    @Test
    void createReservation_skipsEmailWhenEmailIsNull() {
        CreateReservationRequest request = buildRequest(1L, 2);
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(inv -> inv.getArgument(0));

        reservationService.createReservation(request, "uid123", null);

        verify(emailService, never()).sendReservationConfirmation(any(), any());
    }

    @Test
    void createReservation_skipsEmailWhenEmailIsBlank() {
        CreateReservationRequest request = buildRequest(1L, 2);
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(inv -> inv.getArgument(0));

        reservationService.createReservation(request, "uid123", "   ");

        verify(emailService, never()).sendReservationConfirmation(any(), any());
    }

    @Test
    void cancelReservation_setsStatusToCancelled() {
        Reservation existing = new Reservation(1L, "uid123", "user@test.com", 2, "CONFIRMED");
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(inv -> inv.getArgument(0));

        Reservation result = reservationService.cancelReservation(1L, "uid123");

        assertThat(result.getStatus()).isEqualTo("CANCELLED");
    }

    @Test
    void cancelReservation_throwsWhenReservationNotFound() {
        when(reservationRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reservationService.cancelReservation(99L, "uid123"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Reservation not found");
    }

    @Test
    void cancelReservation_throwsWhenUserUidDoesNotMatch() {
        Reservation existing = new Reservation(1L, "uid123", "user@test.com", 2, "CONFIRMED");
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(existing));

        assertThatThrownBy(() -> reservationService.cancelReservation(1L, "uid-other"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("You are not allowed to cancel this reservation");
    }

    @Test
    void getReservationsByUserUid_returnsMatchingReservations() {
        List<Reservation> expected = List.of(
                new Reservation(1L, "uid123", "user@test.com", 2, "CONFIRMED"),
                new Reservation(2L, "uid123", "user@test.com", 1, "CANCELLED"));
        when(reservationRepository.findByUserUid("uid123")).thenReturn(expected);

        List<Reservation> result = reservationService.getReservationsByUserUid("uid123");

        assertThat(result).hasSize(2);
        assertThat(result).allMatch(r -> r.getUserUid().equals("uid123"));
    }
}
