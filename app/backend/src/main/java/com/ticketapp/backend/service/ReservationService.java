package com.ticketapp.backend.service;

import com.ticketapp.backend.dto.CreateReservationRequest;
import com.ticketapp.backend.model.Reservation;
import com.ticketapp.backend.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    // return ticket where the status is not cancelled
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public List<Reservation> getReservationsByUserUid(String userUid) {
        return reservationRepository.findByUserUid(userUid);
    }

    public Reservation createReservation(
            CreateReservationRequest request,
            String userUid,
            String userEmail
    ) {
        Reservation reservation = new Reservation();
        reservation.setEventId(request.getEventId());
        reservation.setQuantity(request.getQuantity());
        reservation.setUserUid(userUid);
        reservation.setUserEmail(userEmail);
        reservation.setStatus("CONFIRMED");

        return reservationRepository.save(reservation);
    }

    public Reservation cancelReservation(Long reservationId, String userUid) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        if (!reservation.getUserUid().equals(userUid)) {
            throw new RuntimeException("You are not allowed to cancel this reservation");
        }

        reservation.setStatus("CANCELLED");
        return reservationRepository.save(reservation);
    }
}